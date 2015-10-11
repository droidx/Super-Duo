package barqsoft.footballscores;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ViewHolder
{
    public LinearLayout detail_layout;
    public TextView home_name;
    public TextView away_name;
    public TextView score;
    public TextView date;
    public ImageView home_crest;
    public ImageView away_crest;
    public double match_id;
    public ViewHolder(View view)
    {
        detail_layout = (LinearLayout) view.findViewById(R.id.detail_fragment_layout);
        home_name = (TextView) view.findViewById(R.id.home_name);
        away_name = (TextView) view.findViewById(R.id.away_name);
        score     = (TextView) view.findViewById(R.id.score_textview);
        date      = (TextView) view.findViewById(R.id.data_textview);
        home_crest = (ImageView) view.findViewById(R.id.home_crest);
        away_crest = (ImageView) view.findViewById(R.id.away_crest);
    }
}
