public interface CosmicComponent {

    //Returns the number of bodies contained in the component and its children.
    int numberOfBodies();

    //Return position of this component
    Vector3 getPosition();

}
