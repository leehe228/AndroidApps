package com.softcon.findway;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultDataAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ResultDataClass> sample;

    public ResultDataAdapter(Context context, ArrayList<ResultDataClass> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public Object getItem(int position) {
        return sample.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        @SuppressLint({"ViewHolder", "InflateParams"}) View view = mLayoutInflater.inflate(R.layout.information_layout, null);

        /* type */
        int type = sample.get(position).getTYPE();

        /* 인스턴스화 */
        LinearLayout outsideLinearLayout = view.findViewById(R.id.Information_LinearLayout_OUTSIDE);

        TextView startTextView = view.findViewById(R.id.Information_TextView_START);
        TextView minTextView = view.findViewById(R.id.Information_TextView_MIN);
        TextView endTextView = view.findViewById(R.id.Information_TextView_END);

        View levelView = view.findViewById(R.id.Information_View_LEVEL);

        LinearLayout info1LinearLayout = view.findViewById(R.id.Information_LinearLayout_INFO1);
        TextView stationNameTextView = view.findViewById(R.id.Information_TextView_StationName);
        ImageView[] linesImageView = new ImageView[2];

        linesImageView[0] = view.findViewById(R.id.Information_ImageView_1);
        ImageView arrowImageView = view.findViewById(R.id.Information_ImageView_leftArrow);
        linesImageView[1] = view.findViewById(R.id.Information_ImageView_2);

        LinearLayout info2LinearLayout = view.findViewById(R.id.Information_LinearLayout_INFO2);
        TextView info1TextView = view.findViewById(R.id.Information_TextView_Info1);
        TextView slashTextView = view.findViewById(R.id.Information_TextView_slash);
        TextView info2TextView = view.findViewById(R.id.Information_TextView_Info2);

        LinearLayout info3LinearLayout = view.findViewById(R.id.Information_LinearLayout_INFO3);
        TextView info3TextView = view.findViewById(R.id.Information_TextView_Info3);

        int level = sample.get(position).getLEVEL();
        switch (level) {
            case 1: {
                levelView.setBackgroundResource(R.color.level1);
                break;
            }
            case 2: {
                levelView.setBackgroundResource(R.color.level2);
                break;
            }
            case 3: {
                levelView.setBackgroundResource(R.color.level3);
                break;
            }
            case 4: {
                levelView.setBackgroundResource(R.color.level4);
                break;
            }
            case 5: {
                levelView.setBackgroundResource(R.color.level5);
                break;
            }
            default: {
                levelView.setBackgroundResource(R.color.level0);
                break;
            }
        }

        /* TYPE 1 & TYPE 2 */
        if (type == 1 || type == 2) {
            //outsideLinearLayout.getLayoutParams().height = 70;

            startTextView.setText(sample.get(position).getSTART());
            minTextView.setVisibility(View.INVISIBLE);
            endTextView.setText(sample.get(position).getEND());

            stationNameTextView.setText(sample.get(position).getSTATION_NAME());

            /* TYPE 1 */
            if (type == 1) {
                int[] lines = new int[2];

                lines[0] = sample.get(position).getLINE1();
                lines[1] = sample.get(position).getLINE2();

                arrowImageView.setImageResource(R.drawable.leftarrow);
                slashTextView.setText(" / ");

                for (int i = 0; i < 2; i++) {
                    switch (lines[i]) {
                        case 1: {
                            linesImageView[i].setImageResource(R.drawable.line1);
                            break;
                        }
                        case 2: {
                            linesImageView[i].setImageResource(R.drawable.line2);
                            break;
                        }
                        case 3: {
                            linesImageView[i].setImageResource(R.drawable.line3);
                            break;
                        }
                        case 4: {
                            linesImageView[i].setImageResource(R.drawable.line4);
                            break;
                        }
                        case 5: {
                            linesImageView[i].setImageResource(R.drawable.line5);
                            break;
                        }
                        case 6: {
                            linesImageView[i].setImageResource(R.drawable.line6);
                            break;
                        }
                        case 7: {
                            linesImageView[i].setImageResource(R.drawable.line7);
                            break;
                        }
                        case 8: {
                            linesImageView[i].setImageResource(R.drawable.line8);
                            break;
                        }
                        case 9: {
                            linesImageView[i].setImageResource(R.drawable.line9);
                            break;
                        }
                        default: {
                            //
                        }
                    }
                }

                info2TextView.setText(sample.get(position).getINFORMATION2());
            }

            /* TYPE 2 */
            else {
                int line = sample.get(position).getLINE1();

                switch (line) {
                    case 1: {
                        linesImageView[0].setImageResource(R.drawable.line1);
                        break;
                    }
                    case 2: {
                        linesImageView[0].setImageResource(R.drawable.line2);
                        break;
                    }
                    case 3: {
                        linesImageView[0].setImageResource(R.drawable.line3);
                        break;
                    }
                    case 4: {
                        linesImageView[0].setImageResource(R.drawable.line4);
                        break;
                    }
                    case 5: {
                        linesImageView[0].setImageResource(R.drawable.line5);
                        break;
                    }
                    case 6: {
                        linesImageView[0].setImageResource(R.drawable.line6);
                        break;
                    }
                    case 7: {
                        linesImageView[0].setImageResource(R.drawable.line7);
                        break;
                    }
                    case 8: {
                        linesImageView[0].setImageResource(R.drawable.line8);
                        break;
                    }
                    case 9: {
                        linesImageView[0].setImageResource(R.drawable.line9);
                        break;
                    }
                    default: {
                        //
                    }
                    linesImageView[1].setVisibility(View.INVISIBLE);

                    slashTextView.setVisibility(View.INVISIBLE);
                    info2TextView.setVisibility(View.INVISIBLE);
                }
            }
        }

        /* TYPE 3 */
        else if (sample.get(position).getTYPE() == 3) {
            outsideLinearLayout.getLayoutParams().height = 120;
            info1LinearLayout.getLayoutParams().height = 0;
            info1LinearLayout.setVisibility(View.INVISIBLE);
            startTextView.getLayoutParams().height = 0;
            endTextView.getLayoutParams().height = 0;

            arrowImageView.setVisibility(View.INVISIBLE);

            minTextView.setText(sample.get(position).getMIN());

            slashTextView.setVisibility(View.INVISIBLE);
            info2TextView.setVisibility(View.INVISIBLE);
        }

        info1TextView.setText(sample.get(position).getINFORMATION1());
        info3TextView.setText(sample.get(position).getINFORMATION3());

        return view;
    }
}
