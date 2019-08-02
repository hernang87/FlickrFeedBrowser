package excercise.hernan.flickrfeedbrowser

interface OnDownloadCompleteListener {
    fun onDownloadComplete(data: String, status: DownloadStatus)
}