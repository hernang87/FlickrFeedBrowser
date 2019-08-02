package excercise.hernan.flickrfeedbrowser

import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), OnDownloadCompleteListener, OnDataAvailableListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate start")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val url = createUri("https://www.flickr.com/services/feeds/photos_public.gne", "android,oreo", "en-us", true)
        val getRawData = GetRawData(this)
        getRawData.execute(url)

        Log.d(TAG, "onCreate end")
    }

    private fun createUri(baseURL: String, searchCriteria: String, lang: String, matchAll: Boolean) : String {
        Log.d(TAG, "createUri")

        return Uri.parse(baseURL)
            .buildUpon()
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("tagsMode",  if(matchAll) "ALL" else "ANY" )
            .build()
            .toString()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "onCreateOptionsMenu")
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected")
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: Success")

            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        } else {
            Log.d(TAG, "onDownloadComplete: Error $status, with data $data")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, "onDataAvailable starts")
        Log.d(TAG, "onDataAvailable ends")
    }

    override fun onError(e: Exception) {
        Log.d(TAG, "onError called ${e.message}")
    }
}
