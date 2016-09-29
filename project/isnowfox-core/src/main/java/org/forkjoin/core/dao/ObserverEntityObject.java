package org.forkjoin.core.dao;

public abstract class ObserverEntityObject extends EntityObject implements ObserverObject {

    private static final long serialVersionUID = -7843687728619974224L;

    private boolean isChange = false;
    private Observer obs;

    @Override
    public boolean isChange() {
        return isChange;
    }

    @Override
    public void onSaveAfter() {
        isChange = true;
    }

    @Override
    public void setObserver(Observer obs) {
        this.obs = obs;
    }

    protected void changeProperty(String name, Object o) {
        isChange = true;
        if (obs != null) {
            obs.changeProperty(name, o);
        }
    }
}
