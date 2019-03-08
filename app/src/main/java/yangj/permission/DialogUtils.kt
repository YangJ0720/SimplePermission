package yangj.permission

import android.app.AlertDialog
import android.content.Context

/**
 * @author YangJ
 */
object DialogUtils {

    /**
     * 显示权限拒绝提示对话框
     */
    fun showPermissionDeniedDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.permission_request)
        builder.setMessage("xxx权限为必选项，全部开通才可以正常使用APP，请到设置中开启")
        builder.setPositiveButton(R.string.go_to_settings) { dialog, which ->
            dialog.dismiss()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, which ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}