package iimmutable;

public class FinalMethodDemo {

    public void drink(){

    }

    public final void eat(){

    }
}

class subClass extends FinalMethodDemo {
    @Override
    public void drink() {
        super.drink();
    }
}
