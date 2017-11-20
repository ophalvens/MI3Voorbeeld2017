package net.ophalvens.mi3voorbeeld2017

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.android.volley.Request
import com.android.volley.toolbox.Volley.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*
import org.json.JSONObject
import org.json.JSONException
//import com.sun.jmx.remote.util.EnvHelp.getCause
import com.android.volley.VolleyError
import org.json.JSONArray
import com.android.volley.Request.Method.POST
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import android.R.attr.fragment
import android.app.Fragment
import android.net.Uri
import android.util.Log


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ToolsFragment.OnFragmentInteractionListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val queue = newRequestQueue(this);
        val url = "http://ophalvens.net/mi3/testdb.php";

        fab.setOnClickListener { view ->
            // toon de snackbar
            Snackbar.make(view, "Retrieving data... Just a moment!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            tv_main.text ="..."

            // Maak JSONObject om mee te geven wanneer de data wordt gevraagd.
            val jObj = JSONObject()
            try {
                //jObj.put("id",32); // indien je een specifieke ID wilt opvragen
                jObj.put("bewerking", "get")
                jObj.put("table", "producten")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val jsObjRequest = JsonObjectRequest(Request.Method.POST, url, jObj,
                    Response.Listener<JSONObject> { response ->
                        tv_main.text = ""
                        val ts = StringBuilder("")
                        var tArray: JSONArray? = null
                        var tObjectRecord: JSONObject? = null

                        try {
                            tArray = response.getJSONArray("data")
                            for (i in 0 until tArray!!.length()) {
                                tObjectRecord = tArray.getJSONObject(i)
                                ts.append("id:" + tObjectRecord!!.getInt("PR_ID") + " - ")
                                ts.append("product:" + tObjectRecord.getString("PR_naam") + " - ")
                                ts.append("prijs:" + tObjectRecord.getString("prijs"))
                                ts.append("\n")
                            }
                            tv_main.text = ts.toString()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }, Response.ErrorListener { error -> tv_main.text = error.cause.toString() })

            // Add the request to the RequestQueue
            queue.add(jsObjRequest)


        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {
                val fragment =  ToolsFragment.newInstance("Een", "Twee")
                val fragmentManager = fragmentManager
                fragmentManager.beginTransaction()
                        .replace(R.id.mainInclude, fragment)
                        .addToBackStack(null)
                        .commit()
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
