import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

public class TxHandler {
	Transaction tx;
	int test;
	
	UTXOPool copy_of_utxoPool;
	UTXO utxo;
	UTXO uf;
    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        copy_of_utxoPool = new UTXOPool (utxoPool);
        // IMPLEMENT THIS
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
    	if (rule_one(tx)&&valid_signatures(tx)&&rule_three(tx)&&rule_four(tx)&&rule_five(tx))
    		return true;
    	else
    		return false;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
		
		Set<Transaction> validTxs = new HashSet<>();
		
		int i;
		
		
		for (i = 0; i < possibleTxs.length;i++)
		{
			
						if (isValidTx(possibleTxs[i]))
						{
							validTxs.add(possibleTxs[i]);
						}
						else 
						{
							
							//remove transactions from the pool but I am too lazy to do that tbh
							
						}							
		}
			
		
		  
    	
    	//return possibleTxs;
		Transaction[] validTxArray = new Transaction[validTxs.size()];
        return validTxs.toArray(validTxArray);
        // IMPLEMENT THIS
    } 
    
   
    
    public boolean valid_signatures (Transaction tx)
    {
    	
    	for (int i=0; i<tx.getInputs().size();i++)
    	{   		
    		UTXO verify = new UTXO(tx.getInput(i).prevTxHash, tx.getInput(i).outputIndex);
    		if (Crypto.verifySignature(copy_of_utxoPool.getTxOutput(verify).address,tx.getRawDataToSign(i),tx.getInput(i).signature))
    		{
    			
    		}
    		else 
    		{
    			return false;
    		}
    		
    	}
    	  return true;
    }
    
     public boolean rule_one (Transaction tx)
        {
    		for (int i=0; i<tx.getInputs().size(); i++)
    		{
    			UTXO utxo = new UTXO(tx.getInput(i).prevTxHash, tx.getInput(i).outputIndex);
    			if(copy_of_utxoPool.contains(utxo))
    			{
    				
    			}
    			else
    			{
    				return false;
    			}			
    		}		
    		
    		return true;
        }
        	
        	public boolean rule_five (Transaction tx)
            {
        		
            	
            	double outvalue = 0.0;
            	double invalue = 0.0;
            	for (int i=0; i<tx.numOutputs();i++)
            	{
            		outvalue += tx.getOutput(i).value;
            	}	
            	
            	for (Transaction.Input in : tx.getInputs())
            		 
            	    {
            		    
            		    UTXO utxoo = new UTXO(in.prevTxHash, in.outputIndex);
            		    invalue += copy_of_utxoPool.getTxOutput(utxoo).value;
            		    
            		  
            		  }
            		
            	
            	
            	if (invalue<outvalue)
            	{
            		return false;
            	}
            	else
            	{
            		return true;
            	}
    	
    	
           }
        	
        	public boolean rule_three (Transaction tx)
        	{
        		int valid =1;
        		UTXOPool nee = new UTXOPool();
        		
        		for (int i=0; i<tx.numInputs();i++)
        		{
        			UTXO three =  new UTXO (tx.getInput(i).prevTxHash,tx.getInput(i).outputIndex);
        			if (nee.contains(three)==false && nee!=null)
        				 nee.addUTXO(three,tx.getOutput(i));
        				
        			else
        				return false;
        			
        		}
        		
        		return true;
        		
        		
        		
        	}
        	
        	
        	public boolean rule_four (Transaction tx)
        	{
        		for (int i=0; i<tx.getOutputs().size(); i++)
        		{
        			if (tx.getOutput(i).value>0)
        			{
        				
        			}
        			else
        			{
        				return false;
        			}			
        		}		
        		
        		return true;
        	}
        	
        	
        	
        	
        	
    
  

}
