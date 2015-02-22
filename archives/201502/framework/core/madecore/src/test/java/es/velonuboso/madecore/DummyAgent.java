/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.velonuboso.madecore;

import com.velonuboso.made.core.common.LabelArchetype;
import com.velonuboso.made.core.common.Position;
import com.velonuboso.made.core.interfaces.MadeAgentInterface;
import java.util.HashSet;

/**
 *
 * @author rhgarcia
 */
public class DummyAgent implements MadeAgentInterface {

    private int id;

    private String log;

    public DummyAgent(int id, String log) {
        this.id = id;
        this.log = log;
    }

    public void addline(int days, String action) {
        log += new String(days + ":" + "@" + action + "\n");
    }

    public int getDays() {
        return 0;
    }

    public HashSet<LabelArchetype> getLabels() {
        return null;
    }

    public Position getPosition() {
        return null;
    }

    public int getProfile() {
        return 0;
    }

    public String getSheet() {
        return null;
    }

    public boolean isAlive() {
        return true;
    }

    public void justLive() {

    }

    public String getStringLog() {
        return log;
    }

    public void addLabel(LabelArchetype label) {
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return "test";
    }

    public String getLabelsAsString() {
        return "test";
    }

}
