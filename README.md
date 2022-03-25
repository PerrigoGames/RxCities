# RxCities

This project is a small simulation designed to test some aspects of ReactiveX:
- Each route between two cities is modeled by two Rx streams:
  - An entrance into which people can be added to begin traversing them to a different city
  - An exit that emits people added to the entrance after a delay and adds them to the destination city
- Each person utilizes three Rx streams:
  - A status stream keeps track of what the person is currently doing, and added statuses determine what to do next
  - An "alive" Completable that tracks when the status stream completes and ignores any other events
  - A stream that provides human-readable status updates that can be logged