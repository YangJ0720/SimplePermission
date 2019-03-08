package yangj.permission

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import yangj.simplepermission.library.PermissionFragment
import yangj.simplepermission.library.PermissionListener

/**
 * @author YangJ
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        button.setOnClickListener {
            onCall("10086")
        }
    }

    /**
     * 拨号
     */
    private fun onCall(phone: String) {
        // 要申请的权限
        val permissions = arrayOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        // 回调监听
        val listener = object : PermissionListener {
            override fun onGranted() {
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$phone")
                startActivity(intent)
            }

            override fun onDenied(permissions: List<String>) {
                DialogUtils.showPermissionDeniedDialog(this@MainActivity)
                permissions.forEach {
                    println("onDenied = $it")
                }
            }
        }
        // 申请权限
        PermissionFragment.requestPermission(this, permissions, listener)
    }

}
