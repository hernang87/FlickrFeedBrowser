package excercise.hernan.flickrfeedbrowser

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL

private const val TAG = "GetRawData"

enum class DownloadStatus {
    OK, IDLE, NOT_INITIALIZED, FAILED_OR_EMPTY, PERMISSIONS_ERROR, ERROR
}

class GetRawData(private val listener: OnDownloadCompleteListener) : AsyncTask<String, Void, String>() {
    private var downloadStatus = DownloadStatus.IDLE

    override fun onPostExecute(result: String) {
        Log.d(TAG, "onPostExecute start")

        listener.onDownloadComplete(result, downloadStatus)

        Log.d(TAG, "onPostExecute end")
    }

    override fun doInBackground(vararg params: String?): String {
        Log.d(TAG, "doInBackground start")

        if (params[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALIZED
            return "No URL specified"
        }

        try {
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        } catch(e: Exception) {
            val errorMessage = when(e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALIZED
                    "doInBackground: Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IO Exception ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    "doInBackground: Permissions error ${e.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "doInBackground: Uknown error ${e.message}"
                }
            }

            Log.d(TAG, errorMessage)

            return errorMessage
        }


        Log.d(TAG, "doInBackground end")
    }
}