package excercise.hernan.flickrfeedbrowser

interface OnDataAvailableListener {
    fun onDataAvailable(data: List<Photo>)
    fun onError(exception: Exception)
}