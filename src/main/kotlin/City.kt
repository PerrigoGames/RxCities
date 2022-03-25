import io.reactivex.schedulers.Schedulers
import kotlin.random.Random

/**
 * Represents a city that [Person] entities can travel between.  Cities:
 * - have no fixed position
 * - are only represented by distances between other cities
 */
class City(
    val name: String,
    val connections: Map<City, Route> = mutableMapOf(),
) {

    val people: MutableList<Person> = mutableListOf()

    private val mutableConnections get() = connections as? MutableMap<City, Route>

    /**
     * Connects two cities together by adding a [Route] from one to the other and vice versa.
     */
    fun connect(
        other: City,
        distance: Int = Random.nextInt(2, 10)
    ) {
        mutableConnections!![other] = connectOneWay(other, distance)
        other.mutableConnections!![this] = other.connectOneWay(this, distance)
    }

    private fun connectOneWay(other: City, distance: Int) = Route(distance).apply {
        exit.observeOn(Schedulers.io())
            .subscribe { it.onArrive(other) }
    }

    /**
     * Pathfinding function that searches for the correct connection to reach another city, if possible.
     */
    fun findNextCity(targetCity: City): City? {
        return findCity(targetCity)
    }

    /**
     * Recursive depth-first function for pathfinding.
     */
    private fun findCity(
        targetCity: City,
        visitedCities: MutableList<City> = mutableListOf()
    ): City? {
        visitedCities += this
        val unvisitedCities = connections.keys.filterNot { visitedCities.contains(it) }
        return if (unvisitedCities.contains(targetCity)) {
            targetCity
        } else {
            unvisitedCities.find { it.findCity(targetCity, visitedCities) != null }
        }
    }

    override fun toString() =
        "$name (${connections.map { "${it.key.name} (${it.value.distance})" }.joinToString()})"

    companion object {

        val CITIES_LIST = listOf(
            "San Francisco",
            "Los Angeles",
            "New York",
            "Portland",
            "Dallas",
            "New Orleans",
            "Seattle",
            "Kansas City",
            "Orlando",
            "Boston",
            "Montreal",
            "Boise",
            "Mexico City",
            "Atlanta",
            "Santa Cruz",
            "Washington D.C."
        )
    }
}