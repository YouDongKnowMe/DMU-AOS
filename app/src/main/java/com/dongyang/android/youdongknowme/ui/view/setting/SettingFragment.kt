package com.dongyang.android.youdongknowme.ui.view.setting

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat
import com.dongyang.android.youdongknowme.R
import com.dongyang.android.youdongknowme.databinding.FragmentSettingBinding
import com.dongyang.android.youdongknowme.standard.base.BaseFragment
import com.dongyang.android.youdongknowme.ui.view.depart.DepartActivity
import com.dongyang.android.youdongknowme.ui.view.keyword.KeywordActivity
import com.dongyang.android.youdongknowme.ui.view.license.LicenseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/* 설정 화면 */
class SettingFragment : BaseFragment<FragmentSettingBinding, SettingViewModel>() {

    override val layoutResourceId: Int = R.layout.fragment_setting
    override val viewModel: SettingViewModel by viewModel()

    private val departmentActivityResultLauncher =
        registerForActivityResult(StartActivityForResult()) { _ ->
            // 학과 선택 화면 이동 후 재진입 시 학과 재조회 처리
            viewModel.getUserDepartment()
        }

    override fun initStartView() {
        binding.tvSettingAppVersion.text = getAppVersion()
        initAlarmSwitchStateByNotificationPermission()
    }

    override fun initDataBinding() {

        viewModel.myDepartment.observe(viewLifecycleOwner) { myDepartment ->
            binding.tvSettingDepartment.text = myDepartment
        }

        viewModel.isAccessUniversityAlarm.observe(viewLifecycleOwner) { isChecked ->
            binding.switchSettingUniversityAlarm.isChecked = isChecked
        }

        viewModel.isAccessDepartAlarm.observe(viewLifecycleOwner) { isChecked ->
            binding.switchSettingDepartmentAlarm.isChecked = isChecked
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading)
                showLoading()
            else
                dismissLoading()
        }
    }

    override fun initAfterBinding() {
        /** TODO 스위치 클릭을 억제하기 위한 임시 뷰, 후에 삭제 필요 */
        binding.viewSettingUniversityAlarm.setOnClickListener {
            if (viewModel.isAccessUniversityAlarm.value == false) {
                if (checkNotificationPermission().not()) {
                    showPermissionDialog()
                    return@setOnClickListener
                }

                if (viewModel.myDepartment.value.isNullOrBlank())
                    return@setOnClickListener

                viewModel.updateUserTopic()
                return@setOnClickListener
            }

            viewModel.removeUserTopic()
        }

        /** TODO 스위치 클릭을 억제하기 위한 임시 뷰, 후에 삭제 필요 */
        binding.viewSettingDepartmentAlarm.setOnClickListener {
            if (viewModel.isAccessDepartAlarm.value == false) {
                if (checkNotificationPermission().not()) {
                    showPermissionDialog()
                    return@setOnClickListener
                }

                if (viewModel.myDepartment.value.isNullOrBlank())
                    return@setOnClickListener

                viewModel.updateUserDepartment()
                return@setOnClickListener
            }

            viewModel.removeUserDepartment()
        }

        binding.btnSettingEditKeyword.setOnClickListener {
            val intent = Intent(requireActivity(), KeywordActivity::class.java)
            startActivity(intent)
        }

        binding.btnSettingEditDepartment.setOnClickListener {
            val intent = Intent(requireActivity(), DepartActivity::class.java)
            departmentActivityResultLauncher.launch(intent)
        }

        binding.btnSettingAppHelp.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://tally.so/r/n9oq91")
            )
            startActivity(intent)
        }

        binding.btnSettingAppPersonalPolicy.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://sites.google.com/view/dmforu-privacy-policy/%ED%99%88")
            )
            startActivity(intent)
        }

        binding.btnSettingAppOpensource.setOnClickListener {
            val intent = Intent(requireActivity(), LicenseActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAppVersion(): String {
        val packageManager =
            requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
        return packageManager.versionName
    }

    private fun initAlarmSwitchStateByNotificationPermission() {
        if (!checkNotificationPermission()) {
            viewModel.setIsAccessDepartAlarm(false)
            viewModel.setIsAccessUniversityAlarm(false)
        }
    }

    private fun showPermissionDialog() {
        val dialog = PermissionDialog(
            title = getString(R.string.dialog_permission_title),
            content = getString(R.string.dialog_permission_content),
            pacakageName = requireContext().packageName,
            cancelListener = {
                viewModel.setIsAccessDepartAlarm(false)
                viewModel.setIsAccessUniversityAlarm(false)
            })

        dialog.show(parentFragmentManager, null)
    }

    private fun checkNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            return true

        if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
            return false
        }

        return true
    }
}
