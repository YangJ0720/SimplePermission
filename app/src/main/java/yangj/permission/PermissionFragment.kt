package yangj.permission

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import java.security.InvalidParameterException

private const val ARG_PARAM_REQUEST_CODE = "request_code"
private const val ARG_PARAM_PERMISSIONS = "permissions"

/**
 * @author YangJ
 */
class PermissionFragment : Fragment() {

    private var mCode = 0
    private var mPermissions: Array<String>? = null

    private var mListener: PermissionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mCode = it.getInt(ARG_PARAM_REQUEST_CODE)
            mPermissions = it.getStringArray(ARG_PARAM_PERMISSIONS)
            mPermissions?.let { it ->
                requestPermissions(it, mCode)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (mCode == requestCode) {
            // 判断是否授权
            val length = permissions.size
            val list = ArrayList<String>(length)
            for (i in 0 until length) {
                if (PackageManager.PERMISSION_DENIED == grantResults[i]) {
                    val hasRefuse = ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissions[i])
                    if (hasRefuse) {
                        list.add(permissions[i])
                    } else {
                        // TODO : 处理永久拒绝权限申请
                    }
                }
            }
            // 回调
            mListener?.let {
                if (list.isEmpty()) {
                    it.onGranted()
                } else {
                    it.onDenied(list)
                }
            }
        }
        // 销毁Fragment
        fragmentManager?.let { it ->
            val transaction = it.beginTransaction()
            transaction.remove(this)
            transaction.commit()
        }
    }

    interface PermissionListener {

        /**
         * 已授权
         */
        fun onGranted()

        /**
         * 未授权
         *
         * @param permissions
         */
        fun onDenied(permissions: List<String>)

    }

    companion object {

        private const val TAG = "BlankFragment"

        @JvmStatic
        private fun newInstance(code: Int, permissions: Array<String>, listener: PermissionListener) =
            PermissionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM_REQUEST_CODE, code)
                    putStringArray(ARG_PARAM_PERMISSIONS, permissions)
                }
                this.mListener = listener
            }

        @JvmStatic
        fun requestPermission(context: Context, code: Int, permissions: Array<String>, listener: PermissionListener) {
            val manager = get(context)
            val transaction = manager.beginTransaction()
            val fragment = newInstance(code, permissions, listener)
            transaction.add(fragment, TAG)
            transaction.commit()
        }

        @JvmStatic
        private fun get(context: Context) : FragmentManager {
            if (context == null) throw NullPointerException("You cannot request permission on a null Context")
            if (context is FragmentActivity) {
                return context.supportFragmentManager
            } else if (context is ContextWrapper) {
                return get(context.baseContext)
            }
            throw InvalidParameterException("Invalid Context")
        }
    }
}
