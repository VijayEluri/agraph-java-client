package org.openrdf.repository.sail;

import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.ValueFactoryImpl;

import franz.exceptions.ServerException;
import franz.exceptions.UnimplementedMethodException;

public class AllegroValueFactory extends ValueFactoryImpl {
	
	private AllegroRepository repository;
	
	public AllegroValueFactory (AllegroRepository repository) {
        this.repository = repository;
//        RDF.initialize(self)
//        RDFS.initialize(self)
//        XMLSchema.initialize(self)
//        OWL.initialize(self)
//        self.store.getConnection().setNamespace("fti", "http://franz.com/ns/allegrograph/2.2/textindex/")
	}
	
	/**
	 * If 'term' is a string, integer, float, etc, convert it to
     * a Literal term.  Otherwise, if its a Value, just pass it through.
	 */
    protected Value objectPositionTermToOpenRDFTerm(Object term, URI predicate) {
    	if (term == null) return (Value)term;
    	if (term instanceof CompoundLiteral) return (CompoundLiteral)term;
    	if (!(term instanceof Value))
    		if (term instanceof String)
    			term = this.createLiteral((String)term);
    		else
    			throw new ServerException("Unanticipated datatype " + term + " passed to 'objectPositionTermToOpenRDFTerm'");
    	    	
        String inlinedType = null;
        if (predicate != null) {
        	inlinedType = this.repository.getInlinedPredicates().get(predicate.stringValue());
        }
        if ((inlinedType == null) && (term instanceof Literal) && (((Literal)term).getDatatype() != null)) {
            inlinedType = this.repository.getInlinedDatatypes().get(((Literal)term).getDatatype());
        }
        if (inlinedType != null) {
            throw new UnimplementedMethodException("Inlined literals are not yet implemented");
            //return EncodedLiteral(term.getLabel(), encoding=inlinedType, store=self.store.internal_ag_store)
        } else {
            return (Value)term;
        }
    }

}
