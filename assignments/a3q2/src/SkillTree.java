import lib280.list.LinkedList280;
import lib280.tree.BasicMAryTree280;

public class SkillTree extends BasicMAryTree280<Skill> {

	/**
	 * Create lib280.tree with the specified root node and
	 * specified maximum arity of nodes.
	 * @timing O(1)
	 * @param x item to set as the root node
	 * @param m number of children allowed for future nodes
	 */
	public SkillTree(Skill x, int m)
	{
		super(x,m);
	}

	/**
	 * A convenience method that avoids typecasts.
	 * Obtains a subtree of the root.
	 *
	 * @param i Index of the desired subtree of the root.
	 * @return the i-th subtree of the root.
	 */
	public SkillTree rootSubTree(int i) {
		return (SkillTree)super.rootSubtree(i);
	}

	/**
	 * Returns a LinkedList280 of skill objects that must be obtained before you can use the skill
	 * @param skillName the name of the desired skill
	 * @return a LinkedList280 of skill prerequisites
	 */
	public LinkedList280<Skill> skillDependencies(String skillName) {
		LinkedList280<Skill> dependencies = new LinkedList280<Skill>();
		if (findSkillDependencies(this, skillName, dependencies)) {
			return dependencies;
		} else {
			throw new RuntimeException(skillName + "not found.");
		}
	}

	private boolean findSkillDependencies(SkillTree root, String skillName, LinkedList280<Skill> dependencies) {
		// Check if this node's skill is the target
		if (root.isEmpty()) {
			return false;
		}

		// Compare the current node's skill with the target skill
		if (root.rootItem().getSkillName().equals(skillName)) {
			dependencies.insert(root.rootItem());
			return true;
		}

		// Iterate over children
		for (int i = 1; i <= root.rootLastNonEmptyChild(); i++) {
			SkillTree subtree = root.rootSubTree(i);
			if (subtree != null && !subtree.isEmpty()) {
				if (findSkillDependencies(subtree, skillName, dependencies)) {
					dependencies.insert(root.rootItem());
					return true;
				}
			}
		}
		return false;
	}

	public int findSkillCost(String skillName) {
		LinkedList280<Skill> dependencies = skillDependencies(skillName);
		int totalCost = 0;
		dependencies.goFirst();
		while (dependencies.itemExists()) {
			totalCost += dependencies.item().getSkillCost();
			dependencies.goForth();
		}
		return totalCost;
	}

	/**
	 * Math method for regression testing
	 * @param args none
	 */
	public static void main(String[] args) {
		Skill elementalMastery = new Skill("Elemental Mastery", "Master all elemental powers.", 1);
		Skill hydrokinesis = new Skill("Hydrokinesis", "Control and shape water.", 1);
		Skill healingFlow = new Skill("Healing Flow", "Accelerate healing using water", 3);
		Skill liquidForm = new Skill("Liquid Form", "Transform into water for mobility", 5);
		Skill pyrokinesis = new Skill("Pyrokinesis", "Generate and manipulate fire.", 1);
		Skill flameImmunity = new Skill("Flame Immunity", "Walk through fire.", 2);
		Skill explosiveCharge = new Skill("Explosive Charge", "Superheat objects to combust on impact", 4);
		Skill aerokinesis = new Skill("Aerokinesis", "Manipulate wind.", 1);
		Skill weightlessness = new Skill("Weightlessness", "Reduce body weight to glide or float", 2);
		Skill vacuumStep = new Skill("Vacuum Step", "Remove air resistance for silent movement", 5);

		SkillTree tree = new SkillTree(elementalMastery, 3);

		// Water branch
		SkillTree waterBranch = new SkillTree(hydrokinesis, 2);
		waterBranch.setRootSubtree(new SkillTree(healingFlow, 2), 1);
		waterBranch.setRootSubtree(new SkillTree(liquidForm, 2), 2);
		tree.setRootSubtree(waterBranch, 1);

		// Fire branch
		SkillTree fireBranch = new SkillTree(pyrokinesis, 2);
		fireBranch.setRootSubtree(new SkillTree(flameImmunity, 2), 1);
		fireBranch.setRootSubtree(new SkillTree(explosiveCharge, 2), 2);
		tree.setRootSubtree(fireBranch, 2);

		// Air branch
		SkillTree airBranch = new SkillTree(aerokinesis, 2);
		airBranch.setRootSubtree(new SkillTree(weightlessness, 2), 1);
		airBranch.setRootSubtree(new SkillTree(vacuumStep, 2), 2);
		tree.setRootSubtree(airBranch, 3);

		System.out.println("My Skill Tree:");
		System.out.println(tree.toStringByLevel());

		// Dependencies for Vacuum Step
		System.out.println("Dependencies for Vacuum Step:");
		try {
			LinkedList280<Skill> deps = tree.skillDependencies("Vacuum Step");
			deps.goFirst();
			while (deps.itemExists()) {
				System.out.print(deps.item().getSkillName() + ", Cost: " + deps.item().getSkillCost() + ", ");
				deps.goForth();
			}
			System.out.println();
		}
		catch (RuntimeException e) {
			System.out.println("Vacuum Step not found.");
		}

		// Dependencies for Hydrokinesis
		System.out.println("Dependencies for Hydrokinesis:");
		try {
			LinkedList280<Skill> deps = tree.skillDependencies("Hydrokinesis");
			deps.goFirst();
			while (deps.itemExists()) {
				System.out.print(deps.item().getSkillName() + ", Cost: " + deps.item().getSkillCost() + ", ");
				deps.goForth();
			}
			System.out.println();
		}
		catch (RuntimeException e) {
			System.out.println("Hydrokinesis not found.");
		}

		// Dependencies for FakeSkill
		System.out.println("Dependencies for FakeSkill:");
		try {
			LinkedList280<Skill> deps = tree.skillDependencies("FakeSkill");
			deps.goFirst();
			while (deps.itemExists()) {
				System.out.print(deps.item().getSkillName() + ", Cost: " + deps.item().getSkillCost() + ", ");
				deps.goForth();
			}
			System.out.println();
		}
		catch (RuntimeException e) {
			System.out.println("FakeSkill not found.");
		}

		System.out.println("To get Explosive Charge you must invest " + tree.findSkillCost("Explosive Charge") + " points.");

		System.out.println("To get Vacuum Step you must invest " + tree.findSkillCost("Vacuum Step") + " points.");

		// Cost to get FakeSkill
		try {
			System.out.println("To get FakeSkill you must invest " + tree.findSkillCost("FakeSkill") + " points.");
		}
		catch (RuntimeException e) {
			System.out.println("FakeSkill not found.");
		}
	}
}
