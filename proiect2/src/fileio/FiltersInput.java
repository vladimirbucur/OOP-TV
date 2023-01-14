package fileio;

public final class FiltersInput {
    private SortInput sort;
    private ContainsInput contains;

    public FiltersInput() {
    }

    /**
     *
     * @return
     */
    public SortInput getSort() {
        return sort;
    }

    /**
     *
     * @param sort
     */
    public void setSort(final SortInput sort) {
        this.sort = sort;
    }

    /**
     *
     * @return
     */
    public ContainsInput getContains() {
        return contains;
    }

    /**
     *
     * @param contains
     */
    public void setContains(final ContainsInput contains) {
        this.contains = contains;
    }
}
