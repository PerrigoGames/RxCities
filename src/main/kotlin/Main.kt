import io.reactivex.Completable

fun main(args: Array<String>) {
    // Generate the initial list of people
    val peopleCount = args.getOrNull(0)?.toIntOrNull() ?: 20
    val people = List(peopleCount) { Person("Person $it") }

    // Generate the initial cities, ensuring they're connected to one existing city
    val cities = City.CITIES_LIST.map { City(it) }
    cities.forEachIndexed { idx, city ->
        val prevCities = cities.subList(0, idx)
        prevCities.randomOrNull()?.connect(city)
    }

    // Print list of cities
    cities.forEach { println(it) }
    println()

    // Timestamp management
    val startTime = System.currentTimeMillis()
    fun timestamp() = "${(System.currentTimeMillis() - startTime) / 1000}s"

    var remainingLives = peopleCount
    people.forEach { person ->
        person.readableStatus.subscribe { status -> // Log readable status
            println("${timestamp()} -- ${person.name} $status")
        }
        person.personLife.subscribe { // Track and log remaining people in transit
            remainingLives -= 1
            println("${timestamp()} -- $remainingLives more remain ${"*".repeat(20)}")
        }

        // Place people into a city, give them a random different target, and start
        person.city = cities.random()
        val targetCity = cities.minus(person.city).random()!!
        person.start(targetCity)
    }

    // Block application from completing until everyone is dead
    Completable.merge(people.map { it.personLife })
        .blockingAwait()
}