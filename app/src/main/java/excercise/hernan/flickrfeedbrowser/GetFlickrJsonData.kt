package excercise.hernan.flickrfeedbrowser

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

private val TAG = "GetFlickrJsonData"

class GetFlickrJsonData(private val listener: OnDataAvailableListener) : AsyncTask<String, Void, ArrayList<Photo>>() {
    override fun doInBackground(vararg params: String?): ArrayList<Photo> {
        Log.d(TAG, "doInBackground: start")

        val photoList = ArrayList<Photo>()

        try {
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("items")

            for (i in 0 until itemsArray.length()) {
                val jsonPhoto = itemsArray.getJSONObject(i)

                val title = jsonPhoto.getString("title")
                val author = jsonPhoto.getString("author")
                val authorId = jsonPhoto.getString("author_id")
                val tags = jsonPhoto.getString("tags")

                val jsonMedia = jsonPhoto.getJSONObject("media")
                val photoUrl = jsonMedia.getString("m")
                val link = photoUrl.replaceFirst("_m.jpg", "_b.jpg")

                val photo = Photo(title, author, authorId, link, tags, photoUrl)
                photoList.add(photo)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.e(TAG, "doInBackground: Error ${e.message}")

            listener.onError(e)
        }

        Log.d(TAG, "doInBackground: end")
        return photoList
    }

    override fun onPostExecute(result: ArrayList<Photo>) {
        Log.d(TAG, "onPostExecute: start")

        super.onPostExecute(result)
        listener.onDataAvailable(result)

        Log.d(TAG, "onPostExecute: end")
    }
}