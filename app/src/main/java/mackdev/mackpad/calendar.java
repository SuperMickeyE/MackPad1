package mackdev.mackpad;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

public class calendar extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub
        return new caldroidCustomAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
    }

}
