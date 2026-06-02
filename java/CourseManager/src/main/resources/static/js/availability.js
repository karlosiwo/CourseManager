function fetchAvailability(courseId, callback) {
    fetch('/api/courses/' + courseId + '/availability')
        .then(response => response.json())
        .then(data => callback(data.availableSeats))
        .catch(err => console.error("Błąd pobierania dostępności:", err));
}