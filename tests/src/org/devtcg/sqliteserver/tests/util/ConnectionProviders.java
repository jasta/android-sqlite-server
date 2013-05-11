package org.devtcg.sqliteserver.tests.util;

import android.content.Context;
import android.content.Intent;
import org.devtcg.sqliteserver.tests.support.SameProcessContentProvider;
import org.devtcg.sqliteserver.tests.support.SameProcessService;
import org.devtcg.sqliteserver.tests.support.SeparateProcessContentProvider;
import org.devtcg.sqliteserver.tests.support.SeparateProcessService;

import java.util.ArrayList;
import java.util.List;

public class ConnectionProviders {
    public static List<ConnectionProvider> sameProcessConnections(Context context) {
        List<ConnectionProvider> providers = new ArrayList<ConnectionProvider>();
        providers.add(sameProcessContentProvider(context));
        providers.add(sameProcessService(context));
        return providers;
    }

    public static List<ConnectionProvider> separateProcessConnections(Context context) {
        List<ConnectionProvider> providers = new ArrayList<ConnectionProvider>();
        providers.add(separateProcessContentProvider(context));
        providers.add(separateProcessService(context));
        return providers;
    }

    public static ConnectionProvider sameProcessContentProvider(Context context) {
        return new ContentProviderConnectionProvider(context,
                SameProcessContentProvider.AUTHORITY);
    }

    public static ConnectionProvider separateProcessContentProvider(Context context) {
        return new ContentProviderConnectionProvider(context,
                SeparateProcessContentProvider.AUTHORITY);
    }

    public static ConnectionProvider sameProcessService(Context context) {
        return new ServiceConnectionProvider(context,
                new Intent(context, SameProcessService.class));
    }

    public static ConnectionProvider separateProcessService(Context context) {
        return new ServiceConnectionProvider(context,
                new Intent(context, SeparateProcessService.class));
    }
}
