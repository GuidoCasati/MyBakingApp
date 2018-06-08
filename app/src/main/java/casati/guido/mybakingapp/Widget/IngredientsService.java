package casati.guido.mybakingapp.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by guido on 05/06/2018.
 */

public class IngredientsService extends RemoteViewsService {


    /**
     * To be implemented by the derived service to generate appropriate factories for
     * the data.
     *
     * @param intent
     */
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new BakingRemoteViewsFactory(this.getApplicationContext()));
    }
}
