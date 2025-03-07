public class AVLTree280 {

    public void insert() {}

    public void delete() {}

    public void search() {}

    public void rotateLeft() {}

    public void rotateRight() {}

    public void rotateLeftRight() {}

    public void rotateRightLeft() {}

//    /**	Form a string representation that includes level numbers.
//     Analysis: Time = O(n), where n = number of items in the (sub)lib280.tree
//     @param i the level for the root of this (sub)lib280.tree
//     */
//    protected String toStringByLevel(int i)
//    {
//        StringBuffer blanks = new StringBuffer((i - 1) * 5);
//        for (int j = 0; j < i - 1; j++)
//            blanks.append("     ");
//
//        String result = new String();
//        if (!isEmpty() && (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty()))
//            result += rootRightSubtree().toStringByLevel(i+1);
//
//        result += "\n" + blanks + i + ": " ;
//        if (isEmpty())
//            result += "-";
//        else
//        {
//            result += rootItem();
//            if (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty())
//                result += rootLeftSubtree().toStringByLevel(i+1);
//        }
//        return result;
//    }
//
//    /**	String representation of the lib280.tree, level by level. <br>
//     Analysis: Time = O(n), where n = number of items in the lib280.tree
//     */
//    public String toStringByLevel()
//    {
//        return toStringByLevel(1);
//    }

    // Main method for regression testing
    public static void main(String[] args) {
    }
}