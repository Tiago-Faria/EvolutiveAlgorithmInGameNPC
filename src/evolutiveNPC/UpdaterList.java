package evolutiveNPC;

import java.util.ArrayList;
import java.util.List;

public class UpdaterList {

    private static List<Updater> list;
    private static List<Updater> toBeRemoved;

    static{
        list = new ArrayList<>();
        toBeRemoved = new ArrayList<>();

    }
    public static void AddUpdater(Updater u){
        list.add(u);
    }
    public static void RemoveUpdater(Updater u){
        toBeRemoved.add(u);
    }

    public static void Update(){
        list.forEach(Updater::Update);
        list.removeAll(toBeRemoved);
    }
}
