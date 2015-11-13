package info.nich.visiblewifipsw;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("关于");
        toolbar.setTitleTextColor(Color.WHITE);

        TextView author, github, blog;
        github = (TextView) findViewById(R.id.githubURL);
        author = (TextView) findViewById(R.id.appAuthor);
        blog = (TextView) findViewById(R.id.blog);
        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://weibo.com/nichbar");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.github.com/nichbar");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://inich.info");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });
    }
}
