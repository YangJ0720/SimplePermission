package yangj.permission

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_CODE_CAMERA = 1

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
            val permissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val listener = object : PermissionFragment.PermissionListener {
                override fun onGranted() {
                    println("onGranted")
                }

                override fun onDenied(permissions: List<String>) {
                    permissions.forEach { it ->
                        println("onDenied = $it")
                    }
                }
            }
            PermissionFragment.requestPermission(this, REQUEST_CODE_CAMERA, permissions, listener)
        }
    }

}
