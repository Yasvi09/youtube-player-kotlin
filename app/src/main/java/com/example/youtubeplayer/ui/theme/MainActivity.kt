package com.example.youtubeplayer.ui.theme

import android.content.ComponentName
import android.content.pm.PackageManager
import com.example.youtubeplayer.R
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var adapter: Adapter
    private lateinit var categoryTagAdapter: CategoryTagAdapter
    private var list: ArrayList<Model> = ArrayList()
    private val categoryTags = listOf(
        "Explore", "All", "New to you", "Gaming", "Test Cr", "Music", "Mixes",
        "Live", "Comedy", "Podcasts", "Computer Science", "Recently uploaded"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerview)
        adapter = Adapter(this, list)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = adapter

        categoryRecyclerView = findViewById(R.id.category_recycler_view)
        categoryTagAdapter = CategoryTagAdapter(this, categoryTags)
        categoryRecyclerView.adapter = categoryTagAdapter

        fetchData()

        val notificationsButton = findViewById<ImageView>(R.id.notifications_button)
        notificationsButton.setOnClickListener {
            changeAppIcon(enableAlias = true)
        }
    }

    private fun fetchData() {
        val requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val url =
            "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=50&key=AIzaSyBDmOhRM42CfZCxu-Z4OSeDTlV-VD0pnGo&type=video"

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            try {
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("items")

                for (i in 0 until jsonArray.length()) {
                    val jsonObject1 = jsonArray.getJSONObject(i)
                    val jsonVideoId = jsonObject1.getJSONObject("id")
                    val jsonObjectSnippet = jsonObject1.getJSONObject("snippet")
                    val jsonThumbnail =
                        jsonObjectSnippet.getJSONObject("thumbnails").getJSONObject("medium")

                    val model = Model(
                        jsonVideoId.getString("videoId"),
                        jsonObjectSnippet.getString("title"),
                        jsonThumbnail.getString("url")
                    )

                    list.add(model)
                }

                if (list.isNotEmpty()) {
                    adapter.notifyDataSetChanged()
                }

            } catch (e: JSONException) {
                Log.e("YouTube", "JSON parsing error: ${e.message}")
            }
        }, { error ->
            Log.e("YouTube", "Network error: ${error.message}")
            Toast.makeText(this, "Error loading videos!", Toast.LENGTH_SHORT).show()
        })

        requestQueue.add(stringRequest)
    }

    private fun changeAppIcon(enableAlias: Boolean) {
        val pm = packageManager
        val mainComponent = ComponentName(this, "com.example.youtubeplayer.ui.theme.MainActivity")
        val aliasComponent = ComponentName(this, "com.example.youtubeplayer.MainActivity_Alias")

        pm.setComponentEnabledSetting(
            if (enableAlias) aliasComponent else mainComponent,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        pm.setComponentEnabledSetting(
            if (enableAlias) mainComponent else aliasComponent,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )

        // Add a confirmation toast
        Toast.makeText(
            this,
            "App icon changed! It may take a moment to update on your launcher.",
            Toast.LENGTH_SHORT
        ).show()
    }

}
