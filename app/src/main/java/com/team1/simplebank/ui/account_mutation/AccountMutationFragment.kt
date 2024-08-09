package com.team1.simplebank.ui.account_mutation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.synrgy.xdomain.model.FilterInput
import com.synrgy.xdomain.model.MutationDataUI
import com.team1.simplebank.R
import com.team1.simplebank.adapter.MutationPagerAdapterV2
import com.team1.simplebank.common.handler.ResourceState
import com.team1.simplebank.common.utils.Converter.toMonthNumber
import com.team1.simplebank.databinding.FragmentAccountMutationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountMutationFragment : Fragment(), OnItemSelectedListener {

    private lateinit var binding: FragmentAccountMutationBinding
    private val accountMutationViewModel: AccountMutationViewModel by viewModels()
    private var adapter: MutationPagerAdapterV2? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountMutationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerview()
        setUpSpinner()
        spinnerClicked()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                accountMutationViewModel.noAccount.collectLatest {value->
                    if (value!=null){
                        accountMutationViewModel.inputFiltering(FilterInput.NoAccount(value))
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                accountMutationViewModel.dataMutationOnUIWithFiltering().collectLatest {
                    adapter?.submitData(it)
                }
            }
        }

    }

    private fun setUpSpinner() {
        val spinnerMonthAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.months_array,
            R.layout.spinner_list_item
        )

        val spinnerTransactionAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.transaction_types_array,
            R.layout.spinner_list_item
        )

        spinnerMonthAdapter.setDropDownViewResource(R.layout.spinner_list_item)
        spinnerTransactionAdapter.setDropDownViewResource(R.layout.spinner_list_item)

        binding.spinnerItemMonth.adapter = spinnerMonthAdapter
        binding.spinnerItemTypeTransaction.adapter = spinnerTransactionAdapter
    }

    private fun spinnerClicked() {
        binding.spinnerItemMonth.onItemSelectedListener = this
        binding.spinnerItemTypeTransaction.onItemSelectedListener = this
    }

    private fun initRecyclerview() {
        binding.rvResultTransaction.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        this.adapter = MutationPagerAdapterV2()
        binding.rvResultTransaction.adapter = this.adapter
        //adapter?.submitList(provideDataManual())

    }

    // fungsi untuk spinner ketika ditekan
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            binding.spinnerItemMonth.id -> {
                val monthPosition = parent.getItemAtPosition(position).toString()
                val month = monthPosition.toMonthNumber()
                accountMutationViewModel.inputFiltering(FilterInput.Month(month))
            }

            binding.spinnerItemTypeTransaction.id -> {
                when (parent.getItemAtPosition(position).toString()) {
                    "Tipe Transaksi" -> {
                        accountMutationViewModel.inputFiltering(FilterInput.Type(null))
                    }

                    "PEMASUKAN" -> {
                        accountMutationViewModel.inputFiltering(FilterInput.Type("PEMASUKAN"))
                    }

                    "PENGELUARAN" -> {
                        accountMutationViewModel.inputFiltering(FilterInput.Type("PENGELUARAN"))
                    }
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}