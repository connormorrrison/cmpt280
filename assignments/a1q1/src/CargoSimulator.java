import lib280.list.LinkedList280;
import java.util.Random;

public class CargoSimulator {

    protected LinkedList280<Ship> fleet;

    private static final int randomSeed = 42;  // Don't modify this!

    /**
     * Construtor will generate a "fixed random" cargo for each ship using the random seed randomSeed.
     *
     * @param numberOfSacks Total number of sacks of grain to be carried by the fleet.
     *                           These are automatically generated.
     *
     * @postcond The instance variable 'fleet' is a list of ships, each one of which contains a list of sacks of cargo.
     */
    public CargoSimulator(int numberOfSacks) {
        // DO NOT MODIFY THE CONSTRUCTOR -- This is generating the data you need to complete the assignment.

        // Seed the random number generator.
        Random generator = new Random(randomSeed);

        // Create the fleet from the ship names in Ship.ShipNames
        this.fleet = new LinkedList280<Ship>();

        for(int i=0; i < Ship.ShipNames.length; i++) {
            this.fleet.insertFirst(new Ship(Ship.ShipNames[i], generator.nextInt(19500)+500 ));
        }

        // Load each ship with some sacks of various grains.
        for(int i=0; i < numberOfSacks; i++) {
            // Generate a random grain type, sack weight, and create the sack object.
            Grain type = Grain.values()[generator.nextInt(Grain.values().length)];
            float weight = generator.nextFloat() * 100;
            Sack thisSack = new Sack(type, weight);

            // Pick a random ship to laod it onto.
            String ship = Ship.ShipNames[generator.nextInt(Ship.ShipNames.length)];

            // Find the ship in the list of ships and load our randomly generated sack of grain.
            this.fleet.goFirst();
            while(this.fleet.itemExists() && this.fleet.item().getName().compareTo(ship) != 0) {
                this.fleet.goForth();
            }
            try {
                this.fleet.item().loadSack(thisSack);
            }
            catch(Exception e) {
                System.out.println("I didn't find a ship named " + ship + ".  That shouldn't happen!");
            }
        }
    }

    /**
     * @return A printable string describing the name of each ship in the fleet and it's contents.
     */
    public String toString() {
        // DO NOT MODIFY THIS METHOD
        return this.fleet.toString();
    }

    public static void main(String[] args) {
        // Create a new cargo simulator object.
        CargoSimulator sim = new CargoSimulator(1000);

        // TODO: Print out how many sacks of wheat each ship is carrying.

        // TODO: Print out a message for each ship that is overloaded.

   }

}
