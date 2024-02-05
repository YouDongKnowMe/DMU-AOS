package com.dongyang.android.youdongknowme.ui.view.search

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.dongyang.android.youdongknowme.R
import com.dongyang.android.youdongknowme.databinding.FragmentSearchBinding
import com.dongyang.android.youdongknowme.standard.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {

    override val layoutResourceId: Int = R.layout.fragment_search
    override val viewModel: SearchViewModel by viewModel()

    override fun initStartView() {
        binding.searchViewModel = viewModel
    }

    override fun initDataBinding() {
        showKeyboardOnEditTextFocus()
        setTextClearButtonVisibility()
        setTextClearButtonClickListener()
    }

    private fun showKeyboardOnEditTextFocus() {
        val searchKeyboard = binding.etSearchBar
        searchKeyboard.requestFocus()

        binding.etSearchBar.post {
            binding.etSearchBar.setSelection(binding.etSearchBar.text.length)
        }

        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.etSearchBar, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setTextClearButtonVisibility() {
        binding.etSearchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.validateSearchContent()
            }
        })

        viewModel.searchValidation.observe(viewLifecycleOwner) { isValid ->
            binding.ivSearchCancel.visibility = if (isValid) View.VISIBLE else View.GONE
        }
    }

    private fun setTextClearButtonClickListener() {
        with(binding) {
            ivSearchCancel.setOnClickListener {
                etSearchBar.text.clear()
            }
        }
    }

    override fun initAfterBinding() {
    }

}