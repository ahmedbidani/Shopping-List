package com.mayokun.shoppinglist.ui.home


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mayokun.shoppinglist.R
import com.mayokun.shoppinglist.database.ShoppingItem
import com.mayokun.shoppinglist.database.ShoppingItemDatabase
import com.mayokun.shoppinglist.databinding.FragmentHomeBinding



/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var homeFragmentViewModel: HomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home
            , container, false
        )


        val dataSource = ShoppingItemDatabase.getInstance(this.requireContext()).shoppingItemDao

        val homeFragmentViewModelFactory = HomeFragmentViewModelFactory(dataSource)

        homeFragmentViewModel = ViewModelProviders.of(this,homeFragmentViewModelFactory)
            .get(HomeFragmentViewModel::class.java)

        //Observer to check if the database contains any items
        homeFragmentViewModel.hasContent.observe(this, Observer { state ->
            if (state){
                //TODO: Show list of items
            }
        })

        binding.homeFragmentViewModel = homeFragmentViewModel
        binding.lifecycleOwner = this

        //Onclick listener for the FAB
        binding.newItemButton.setOnClickListener { createPopUp() }

        return binding.root
    }

    /**
     * This shows the custom dialog for adding a new item
     */
    @SuppressLint("InflateParams")
    private fun createPopUp() {
        val builder = AlertDialog.Builder(context)
        val alertDialog: AlertDialog
        val view = layoutInflater.inflate(R.layout.create_new_item,null)
        builder.setView(view)
        alertDialog = builder.create()
        alertDialog.show()

        view.findViewById<Button>(R.id.save_item_button).setOnClickListener {
            onSaveButtonPressed(view)
            alertDialog.dismiss()
        }

    }

    private fun onSaveButtonPressed(view: View) {
        val name = view.findViewById<EditText>(R.id.itemNameID)?.text.toString()
        val quantity = view.findViewById<EditText>(R.id.itemQuantityID)?.text.toString().toInt()

        val item = ShoppingItem(itemName = name,itemQuantity = quantity)

        homeFragmentViewModel.onSaveButtonPressed(item)

    }


}
