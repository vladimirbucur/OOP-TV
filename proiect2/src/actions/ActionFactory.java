package actions;

public class ActionFactory {
    public enum ActionType {
        Back, Login, Logout, Movies, Recommendations, Register, SeeDetails, Upgrades
    }

    /**
     *
     * @param actionType
     * @return
     */
    public Action createAction(final ActionType actionType) {
        switch (actionType) {
            case Back: return new Back();
            case Login: return new Login();
            case Logout: return new Logout();
            case Movies: return new Movies();
            case Recommendations: return new Recommendations();
            case Register: return new Register();
            case SeeDetails: return new SeeDetails();
            case Upgrades: return new Upgrades();
            default: return null;
        }
    }
}
