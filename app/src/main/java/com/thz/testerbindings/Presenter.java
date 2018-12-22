package com.thz.testerbindings;

import com.thz.testablebinding.Expose;

/**
 * Created by Talha Hasan Zia on 16-Mar-18.
 * <p></p><b>Description:</b><p></p> Why class was created?
 * <p></p>
 * <b>Public Methods:</b><p></p> Only listing to public methods usage.
 */
@Expose
public class Presenter {

    public int cube(int n) {
        return n * n * n;
    }


    private int square(int n)
    {

        return n*n;
    }

}
