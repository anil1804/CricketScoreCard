package com.thenewcone.myscorecard.intf;

import java.util.List;

public interface ListInteractionListener {
    void onListFragmentInteraction(Object selItem);
    void onListFragmentMultiSelect(List<Object> selItemList);
}
