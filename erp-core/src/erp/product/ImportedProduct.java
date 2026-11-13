package erp.product;

public class ImportedProduct extends Product{

    private String _country;
    private double _TAX;

    public ImportedProduct(int id, String name,Category c, int basePrice, int baseShipping,
                            String country, double tax){
        super(id, name, c, basePrice, baseShipping);
        _country = country;
        _TAX = tax;
    }

    /** GETTERS */

    public String getCountry(){
        return _country;
    }

    public double getTAX(){
        return _TAX;
    }

    /** SETTERS */

    public void setCountry(String newCountry){
        _country = newCountry;
    }

    public void setTAX(double newTAX){
        _TAX = newTAX;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ImportedProduct){
            ImportedProduct p = (ImportedProduct) o;
            return (_id == p.getID() &&
                    _name.equals(p.getName()) &&
                    _category.equals(p.getCategory()) &&
                    _basePrice == p.getBasePrice() &&
                    _baseShippingTime == p.getBaseShippingTime() &&
                    getType().equals("IMPORTADO"));
        }
        return false;
    }
    
    @Override 
    public String toString(){
        return _id + " - " + getType() + " - "+ _name + " - " + _category + " - " + _basePrice + " - " +
         String.format("%.2f", getRecommendedPrice()) + " - " + _baseShippingTime + " - " + _totalUnitsAvailable + " - " + getAdditionalInfo() ;
    }

    @Override
    public double getRecommendedPrice(){
        return (_basePrice * (100 + _TAX))/100;    /** Não é arredondado, mas apresenta-se com 2 casas */
    }

    @Override 
    public String getType(){
        return "IMPORTADO";
    }

    @Override
    public String getAdditionalInfo(){
        return _country + " - " +  _TAX;
    }
}