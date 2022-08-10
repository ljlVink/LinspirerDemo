package activitylauncher;

import android.content.ComponentName;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.huosoft.wisdomclass.linspirerdemo.R;

public class RootLauncherActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle == null) {
                return;
            }

            String pkg = bundle.getString("pkg");
            String cls = bundle.getString("cls");
            String signature = bundle.getString("sign");

            var componentName = new ComponentName(pkg, cls);

            var signer = new Signer(getApplicationContext());
            if (signer.validateComponentNameSignature(componentName, signature)) {
                LauncherIconCreator.launchActivity(getApplicationContext(), componentName, true);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getText(R.string.error).toString() + ": " + e, Toast.LENGTH_LONG).show();
        } finally {
            finish();
        }
    }
}
