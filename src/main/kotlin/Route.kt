import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Represents a connection between two [City] entities.
 */
class Route(
    val distance: Int,
) {

    /**
     * Used by [Person], publishing to this subject will cause a person to "enter" the route, to be emitted
     * by [exit] at the end of the simulated travel period.
     */
    val enter: PublishSubject<Person> = PublishSubject.create()

    /**
     * Observed by [City], will emit [Person] entities when those entities complete traveling and arrive at
     * the destination.
     */
    val exit: Observable<Person> = enter
        .delay(distance.toLong(), TimeUnit.SECONDS)
        .hide()
}