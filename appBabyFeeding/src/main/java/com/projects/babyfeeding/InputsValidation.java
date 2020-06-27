package com.projects.babyfeeding;

public class InputsValidation {
	
	public static Double doubleValidation(String input)
	{	
		Double output;
		if(input.equals(""))
		{
			output=0.0;
		}
		else
		{	output=Double.parseDouble(input);
		}
		return output;
	}
	
	public static int intValidation(String input)
	{	
		int output;
		if(input.equals(""))
		{
			output=0;
		}
		else
		{	output=Integer.parseInt(input);
		}
		return output;
	}
	
	public static boolean scenarioValidation(int ldi, int rdi, int bfi, int efi)
	{
      /*	if((ldi==0)&&(rdi>0))
    	{
    		rightChecked=true;
    		leftChecked=false;
    	}
    	if((ldi>0)&&(rdi==0))
    	{
    		leftChecked=true;
    		rightChecked=false;
    	}
    	if((ldi>0)&&(rdi>0))
    	{
    		if(lr!=null)
        	{
        		leftChecked=lr.isChecked();
        		Log.i("onClicked-rb:","rb3");
        	}
        	if(rr!=null)
        	{
        		rightChecked=rr.isChecked();
        		Log.i("onClicked-rb:","rb4");
        	}
    	}
    	if((ldi==0)&&(rdi==0))
    	{
    		
    	}*/
		return false;
	}
	
	public static boolean orderInputValidation()
	{
		return false;
	}
	

}
