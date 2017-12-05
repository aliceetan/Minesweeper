package hu.ait.android.minesweeper;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.facebook.shimmer.ShimmerFrameLayout;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layoutContent;
    private boolean flagging;
    private TextView winLosetxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        winLosetxt = (TextView) findViewById(R.id.winLosetxt);
        winLosetxt.setTextColor(Color.parseColor("#993399"));
        layoutContent = (LinearLayout) findViewById(R.id.layoutContent);
        ToggleButton btnToggle = (ToggleButton) findViewById(R.id.btnToggle);
        final MinesweeperView minesweeperView = (MinesweeperView) findViewById(R.id.minesweeperView);
        Button btnRestart = (Button) findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minesweeperView.clearBoard();
            }
        });
        btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    flagging = true;
                } else {
                    flagging = false;
                }
            }

        });
        Button btnHint = (Button) findViewById(R.id.btnHint);
        btnHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(layoutContent, minesweeperView.showHint(), Snackbar.LENGTH_LONG).show();
            }
        });

        ShimmerFrameLayout shimmerView = findViewById(R.id.shimmer_view);
        shimmerView.startShimmerAnimation();
    }

    public void youWinLose(String status) {
        winLosetxt.setText(status);

    }

    public boolean buttonStatus() {
        return flagging;
    }


}
