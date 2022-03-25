/**
 * Describes the state a [Person] can be in.
 */
sealed class PersonState {
    object Started: PersonState()
    class Departed(val destination: City): PersonState()
    class Arrived(val destination: City): PersonState()
    object Finished: PersonState()
}