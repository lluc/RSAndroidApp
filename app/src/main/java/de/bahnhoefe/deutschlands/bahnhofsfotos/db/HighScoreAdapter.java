package de.bahnhoefe.deutschlands.bahnhofsfotos.db;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import de.bahnhoefe.deutschlands.bahnhofsfotos.R;
import de.bahnhoefe.deutschlands.bahnhofsfotos.databinding.ItemHighscoreBinding;
import de.bahnhoefe.deutschlands.bahnhofsfotos.model.HighScoreItem;

public class HighScoreAdapter extends ArrayAdapter<HighScoreItem> {
    private final Activity context;
    private List<HighScoreItem> highScore;
    private HighScoreFilter filter;

    public HighScoreAdapter(final Activity context, final List<HighScoreItem> highScore) {
        super(context, R.layout.item_highscore, highScore);
        this.highScore = highScore;
        this.context = context;
    }

    @Override
    public View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        final ItemHighscoreBinding binding;
        if (rowView == null) {
            final LayoutInflater inflater = context.getLayoutInflater();
            binding = ItemHighscoreBinding.inflate(inflater, parent, false);
            rowView = binding.getRoot();
            rowView.setTag(binding);
        } else {
            binding = (ItemHighscoreBinding) rowView.getTag();
        }

        final HighScoreItem item = highScore.get(position);
        binding.highscoreName.setText(item.getName());
        binding.highscorePhotos.setText(String.valueOf(item.getPhotos()));
        binding.highscorePosition.setText(String.valueOf(item.getPosition()).concat("."));

        switch (item.getPosition()) {
            case 1:
                binding.highscoreAward.setImageResource(R.drawable.ic_crown_gold);
                binding.highscoreAward.setVisibility(View.VISIBLE);
                binding.highscorePosition.setVisibility(View.INVISIBLE);
                break;
            case 2:
                binding.highscoreAward.setImageResource(R.drawable.ic_crown_silver);
                binding.highscoreAward.setVisibility(View.VISIBLE);
                binding.highscorePosition.setVisibility(View.INVISIBLE);
                break;
            case 3:
                binding.highscoreAward.setImageResource(R.drawable.ic_crown_bronze);
                binding.highscoreAward.setVisibility(View.VISIBLE);
                binding.highscorePosition.setVisibility(View.INVISIBLE);
                break;
            default:
                binding.highscoreAward.setVisibility(View.INVISIBLE);
                binding.highscorePosition.setVisibility(View.VISIBLE);
                break;
        }

        if (position % 2 == 1) {
            rowView.setBackgroundResource(R.drawable.item_list_backgroundcolor);
        } else {
            rowView.setBackgroundResource(R.drawable.item_list_backgroundcolor2);
        }

        return rowView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new HighScoreFilter(highScore);
        }
        return filter;
    }

    private class HighScoreFilter extends Filter {

        private final List<HighScoreItem> originalItems = new ArrayList<>();

        public HighScoreFilter(final List<HighScoreItem> originalItems) {
            this.originalItems.addAll(originalItems);
        }

        @Override
        protected FilterResults performFiltering(final CharSequence constraint) {
            final FilterResults filterResults = new FilterResults();
            final ArrayList<HighScoreItem> tempList = new ArrayList<>();

            if (constraint != null) {
                final String search = constraint.toString().toLowerCase();
                final int length = originalItems.size();
                int i = 0;
                while (i<length) {
                    final HighScoreItem item = originalItems.get(i);
                    if (item.getName().toLowerCase().contains(search))
                    tempList.add(item);
                    i++;
                }
                filterResults.values = tempList;
                filterResults.count = tempList.size();
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(final CharSequence contraint, final FilterResults results) {
            highScore = (ArrayList<HighScoreItem>) results.values;
            clear();
            addAll(highScore);
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

}
