package com.lyz.lib.view.map;

import com.lyz.lib.base.BaseResultBean;

import java.util.List;

/**
 * Created by lyz on 2018/8/17.
 *
 */

public class LocationBean extends BaseResultBean {

    private List<Location> locationData;

    public List<Location> getLocationData() {
        return locationData;
    }

    public void setLocationData(List<Location> locationData) {
        this.locationData = locationData;
    }
}
