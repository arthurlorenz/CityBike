/*
 * Copyright © 2016  Arthur Lorenz (arthur.w.lorenz@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.lorenz.arthur.citybike;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

public class CopyrightActivity extends AppCompatActivity {

    private ActionBar actionBar;

    //Copyright text
    private final String COPYRIGHT = "Copyright (c) 2016\n" +
                                     "Arthur Lorenz\n" +
                                     "arthur.w.lorenz@gmail.com\n " +
                                     "https://github.com/arthurlorenz/\n\n" +
                                     "Logo-Datenquelle: Stadt Wien – data.wien.gv.at\n\n" +
                                     "This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.\n\n" +
                                     "This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.\n<http://www.gnu.org/licenses/>\n\n " +
                                     "Hiermit wird klargestellt, dass\n  (a) Citybike Wien und die Gewista Werbeges.m.b.H. nicht für den Inhalt der Seite, des Services oder der Applikation verantwortlich sind\n  (b) Citybike Wien und die Gewista Werbeges.m.b.H. in keiner Weise mit dem/der/den BetreiberInnen in Verbindung steht\n  (c) Citybike Wien und die Gewista Werbeges.m.b.H. keine Verantwortung über Richtigkeit oder Aktualität der Daten übernimmt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copyright);

        //set actionbar
        actionBar = getSupportActionBar();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        //set TextView
        TextView copyRight = (TextView) findViewById(R.id.textView2);
        copyRight.setText(COPYRIGHT);
        copyRight.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
