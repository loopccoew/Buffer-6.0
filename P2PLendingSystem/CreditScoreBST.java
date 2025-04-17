package dsa_modules;

import dao.BorrowerDAO;
import dao.LenderDAO;
import java.util.ArrayList;
import java.util.List;

public class CreditScoreBST {
    public Borrower root;
    private BorrowerDAO borrowerDAO;

    // Default constructor (no DB usage)
    public CreditScoreBST() {
        this.borrowerDAO = null;
    }

    // Constructor for DB-enabled version
    public CreditScoreBST(BorrowerDAO borrowerDAO) {
        this.borrowerDAO = borrowerDAO;
    }

    public void insert(Borrower b) {
        root = insertRec(root, b);
        System.out.println("📌 Inserted Borrower: " + b.getName() + " | Credit Score: " + b.getCreditScore() +
                " | Requested Amount: " + b.getRequestedAmount() + " | Requested Rate: " + b.getRequestedRate());

        if (borrowerDAO != null) {
            borrowerDAO.saveBorrower(b); // Save to DB if DAO is provided
        }
    }

    private Borrower insertRec(Borrower root, Borrower b) {
        if (root == null) return b;
        if (b.getCreditScore() < root.getCreditScore()) {
            root.left = insertRec(root.left, b);
        } else {
            root.right = insertRec(root.right, b);
        }
        return root;
    }

    // In-order print (optional)
    public void inorder(Borrower root) {
        if (root != null) {
            inorder(root.left);
            System.out.println(root.getName() + ": " + root.getCreditScore());
            inorder(root.right);
        }
    }

    public List<Borrower> getSortedBorrowers() {
        List<Borrower> list = new ArrayList<>();
        inorderHelper(root, list);
        return list;
    }

    private void inorderHelper(Borrower node, List<Borrower> list) {
        if (node != null) {
            inorderHelper(node.left, list);
            list.add(node);
            inorderHelper(node.right, list);
        }
    }

    // 🔄 Matching logic: true if lender can support borrower's request
    public static boolean isMatch(Lender lender, Borrower borrower) {
        return lender.getMaxAmount() >= borrower.getRequestedAmount() &&
               borrower.getRequestedRate() >= lender.getPreferredRate();
    }

    // 🔍 Match all borrowers in BST against a list of lenders
    public void matchLenders(List<Lender> lenders) {
        List<Borrower> borrowers = getSortedBorrowers();

        for (Borrower borrower : borrowers) {
            boolean foundMatch = false;
            for (Lender lender : lenders) {
                if (isMatch(lender, borrower)) {
                    System.out.println("✅ Match Found: Borrower " + borrower.getName() + 
                                       " ↔ Lender " + lender.getName());
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch) {
                System.out.println("❌ No match for Borrower: " + borrower.getName());
            }
        }
    }
}
