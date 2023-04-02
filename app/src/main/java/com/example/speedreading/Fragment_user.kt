package com.example.speedreading

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [fragment_user.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_user : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var spinner: Spinner
    private lateinit var bundle:Bundle
    private lateinit var showText: TextView
    private lateinit var user_id:String
    private lateinit var email: String

    private var usersApi = Retrofit.Builder().baseUrl("http://192.168.1.64:8080").addConverterFactory(
        GsonConverterFactory.create()).build().create(com.example.speedreading.usersApi::class.java)
    private var performanceApi = Retrofit.Builder().baseUrl("http://192.168.1.64:8080").addConverterFactory(
        GsonConverterFactory.create()).build().create(com.example.speedreading.performanceApi::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_user, container, false)
        // Inflate the layout for this fragment
        bundle = requireArguments()
        if(bundle!=null){
            user_id = bundle.getString("key").toString()
            email = bundle.getString("email").toString()

        }
        showText = view.findViewById(R.id.textView27)
        spinner = view.findViewById(R.id.spinner)
        Log.d("test", email)
GlobalScope.launch {
    activity?.runOnUiThread {
        changetext(email)
        val adapter = getContext()?.let {
            ArrayAdapter.createFromResource(
                it, R.array.exercises,
                android.R.layout.simple_spinner_item
            )
        }
        if (adapter != null) {
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        }
        spinner.adapter = adapter
    }
}

            //changetext(email)
//        if (container != null) {
//            ArrayAdapter.createFromResource(
//                container.context,
//                R.array.exercises,
//                android.R.layout.simple_spinner_item
//            ).also { adapter ->
//                // Specify the layout to use when the list of choices appears
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                // Apply the adapter to the spinner
//                activity?.runOnUiThread { addAdapter(adapter) }
//
//            }
//        }

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0 != null) {
                    var item = p0.getItemAtPosition(p2)
                    Log.d("test", item.toString())
                    if(item.toString() == "Schulte Table"){
                        drawGraph(1)
                    } else if(item.toString() == "Line of Sight"){
                        drawGraph(2)
                    } else if(item.toString() =="3"){
                        drawGraph(3)
                    }
                    else if(item.toString() =="4"){
                        drawGraph(4)
                    }
                    else if(item.toString() =="5"){
                        drawGraph(5)
                    }

                    //drawGraph(item)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d("test", "nothing selected")
            }
        }
        return view
    }
    private fun drawGraph(item: Int){
        GlobalScope.launch{
            var performances = performanceApi.getAllPerformance().await()
            var series: LineGraphSeries<DataPoint> = LineGraphSeries()
            performances.forEach{
                if(user_id == it.userId && it.exerciseId == item){
                    var formatter = SimpleDateFormat("E MMM dd HH:mm:ss z yyyy")
                    Log.d("test", formatter.parse(it.date).toString())
                    var datapoint = DataPoint(formatter.parse(it.date), it.score.toDouble())
                    series.appendData(datapoint, true, 10)
                }
            }
            var graph: GraphView? = view?.findViewById(R.id.graph)
            if (graph != null) {
                graph.title = "Performance"
                graph.removeAllSeries()
                graph.addSeries(series)
                graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(activity)
            }

        }
    }
    private fun changetext(text:String){
        var textview = view?.findViewById<TextView>(R.id.textView27)
        if (textview != null) {
            textview.textSize = 30F
            textview.text = "Welcome, \n"+text+"!"
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_user.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_user().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}