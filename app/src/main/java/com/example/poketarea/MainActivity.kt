package com.example.poketarea

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.poketarea.models.Pokemon
import kotlinx.android.synthetic.main.activity_main.*

import com.example.poketarea.utilities.NetworkUtils
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FetchPokemonTask().execute()
        //initRecycler()
    }


    fun initRecycler(pokemonInfo: String?) {

        var pokemon: MutableList<Pokemon> = MutableList(19) {i ->
            Pokemon(i,"Name: " + pokemonInfo, "Type " + i)
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = PokemonAdapter(pokemon)

        rv_pokemon_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    private class FetchPokemonTask : AsyncTask<String, Void, String>() {

        protected override fun doInBackground(vararg pokemonNumbers: String): String? {

            if (pokemonNumbers.size == 0) {
                return null
            }

            val ID = pokemonNumbers[0]

            val pokeAPI = NetworkUtils.buildUrl(ID)

            try {
                return NetworkUtils.getResponseFromHttpUrl(pokeAPI!!)
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }

        }

        override fun onPostExecute(pokemonInfo: String?) {
            if (pokemonInfo != null || pokemonInfo != "") {
                //mResultText.setText(pokemonInfo)
                MainActivity().initRecycler(pokemonInfo)
            } else {
                //mResultText.setText(getString(R.string.text_not_found_message))

            }
        }

    }

}
