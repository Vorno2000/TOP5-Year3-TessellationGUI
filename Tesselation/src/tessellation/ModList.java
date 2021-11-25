/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tessellation;

import java.util.List;

/**
 *
 * @author https://stackoverflow.com/questions/18659792/circular-arraylist-extending-arraylist
 */

//this and listWrapper are a modified list that acts as a circular list and 
//is used to search in cases where the number would go over size(), it would
//loop back to the first index and allows for more efficiency
public class ModList<T> extends ListWrapper<T> {
    public ModList(List<T> list) {
        super(list);
    }

    @Override
    public T get(int index) {
        int listSize = wrapped.size();
        int indexToGet = index % listSize;

        indexToGet = (indexToGet<0) ? indexToGet+listSize : indexToGet;
        return wrapped.get(indexToGet);
    }
}
