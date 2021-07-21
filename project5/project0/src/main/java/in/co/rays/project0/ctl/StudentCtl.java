package in.co.rays.project0.ctl;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import in.co.rays.project0.dto.StudentDTO;
import in.co.rays.project0.exception.ApplicationException;
import in.co.rays.project0.exception.DuplicateRecordException;
import in.co.rays.project0.form.StudentForm;
import in.co.rays.project0.service.CollegeServiceInt;
import in.co.rays.project0.service.StudentServiceInt;

@Controller
@RequestMapping(value="/ctl/Student")
public class StudentCtl extends BaseCtl 
{
   
	@Autowired
	private StudentServiceInt service;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private CollegeServiceInt cservice;
	  @InitBinder
	    public void initBinder ( WebDataBinder binder )
	    {
	        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
	        binder.registerCustomEditor(String.class, stringtrimmer);
	    }
	    
	
	@Override
	public void preload(Model model) 
	{
	 List list = cservice.search(null);
	 model.addAttribute("collegeList", list);
	}
	
	@RequestMapping(value="/AddStudent" , method=RequestMethod.GET)
	public String display(@RequestParam(required=false) Long id , @ModelAttribute("form") StudentForm form , Model model,Locale locale)
	{
		String enteremail=messageSource.getMessage("label.enteremail",null,locale);
		model.addAttribute("enteremail",enteremail);
		 
		String enterdob=messageSource.getMessage("label.enterdob",null,locale);
		model.addAttribute("enterdob",enterdob);
		 
		String enterfirstName=messageSource.getMessage("label.enterfname",null,locale);
		model.addAttribute("enterfirstName",enterfirstName);
		 
		String enterLastName=messageSource.getMessage("label.enterlname",null,locale);
		model.addAttribute("enterLastName",enterLastName);

		 
		String enterMobile=messageSource.getMessage("label.entermob",null,locale);
		model.addAttribute("enterMobile",enterMobile);  
		
		if(id!=null && id>0)
		{
			StudentDTO dto = service.findById(id);
			form.populate(dto);
		}
		
		return "StudentView";
	}
	
	@RequestMapping(value="/AddStudent" , method=RequestMethod.POST)
	public String submit(@ModelAttribute("form") @Valid StudentForm form,BindingResult result,Model model , Locale locale) throws DuplicateRecordException, ApplicationException
	{
		String enteremail=messageSource.getMessage("label.enteremail",null,locale);
		model.addAttribute("enteremail",enteremail);
		 
		String enterdob=messageSource.getMessage("label.enterdob",null,locale);
		model.addAttribute("enterdob",enterdob);
		 
		String enterfirstName=messageSource.getMessage("label.enterfname",null,locale);
		model.addAttribute("enterfirstName",enterfirstName);
		 
		String enterLastName=messageSource.getMessage("label.enterlname",null,locale);
		model.addAttribute("enterLastName",enterLastName);

		 
		String enterMobile=messageSource.getMessage("label.entermob",null,locale);
		model.addAttribute("enterMobile",enterMobile);  
		
		if(OP_SAVE.equalsIgnoreCase(form.getOperation()))
		{
			if(result.hasErrors())
			{
				return "StudentView";
			}
			
			StudentDTO dto = (StudentDTO) form.getDto();
			
			if(dto.getId()>0)
			{
				service.update(dto);
				String msg = messageSource.getMessage("message.updatesuccess", null, locale);
				model.addAttribute("success", msg);
			}else
			{
				try 
				{
					dto.setCreatedBy("root");
					dto.setModifiedBy("root");
					service.add(dto);
					String msg = messageSource.getMessage("message.success", null, locale);
					model.addAttribute("success", msg);
				} catch (DuplicateRecordException e) 
				{
					String msg = messageSource.getMessage("message.email", null, locale);
					model.addAttribute("error", msg);
				}	
			}
		}
		
		if(OP_RESET.equalsIgnoreCase(form.getOperation()))
		{
			return "redirect:/ctl/Student/AddStudent";
		}
		if(OP_CANCEL.equalsIgnoreCase(form.getOperation()))
		{
			return "redirect:/ctl/Student/StudentListCtl";
		}
		
		return "StudentView";
	}
	
	
	/**
     * Display StudentList View
     * 
     * param form:
     * 				Object of StudentForm
     * param model:
     * 				Object of Model Interface
     * return StudentList:
     * 						View of StudentList
     */
	@RequestMapping(value="/StudentListCtl", method=RequestMethod.GET)
	public String display(@ModelAttribute("form") StudentForm form, Model model,Locale locale) 
	{
		String enterfirstName=messageSource.getMessage("label.enterfname",null,locale);
		model.addAttribute("enterfirstName",enterfirstName);
		
		model.addAttribute("list",service.search(new StudentDTO(), form.getPageNo(),form.getPageSize()));
		int pageNo = 1;
		
		List next = service.search(new StudentDTO(), pageNo+1, form.getPageSize());
		model.addAttribute("nextlistsize",next.size());
/*        model.addAttribute("findList",service.search(null));*/
		
		return "StudentListView";
	}
	
	
	/**
     * Submit StudentList 
     * 
     * param operation:
     * 					Operation given by User
     * param form:
     * 				Object of StudentForm
     * param model:
     * 				Object of Model Interface
     * param locale:
     * 				Object of Locale
     * return StudentList:
     * 						View of Student List
     */
	@RequestMapping(value="/StudentListCtl",method=RequestMethod.POST)
	public String submit(@RequestParam(required=false) String operation, @ModelAttribute("form") StudentForm form, Model model, Locale locale) 
	{
		
		String enterfirstName=messageSource.getMessage("label.enterfname",null,locale);
		model.addAttribute("enterfirstName",enterfirstName);
		
		//model.addAttribute("findList",service.search(null));
		
		int pageNo=form.getPageNo();
		
		if(OP_PREVIOUS.equalsIgnoreCase(operation)) 
		{
			pageNo--;
		}else if(OP_NEXT.equalsIgnoreCase(operation)) 
		{
			pageNo++;
		}
		if(OP_DELETE.equalsIgnoreCase(operation))
		{
			
			if(form.getChk_1()!=null)
			   {
				   for(long id:form.getChk_1()){
		    			service.delete(id);
		    		}
				   String msg = messageSource.getMessage("message.deleterecord", null, locale);
					model.addAttribute("success", msg);
		    	}else {
		    		String msg = messageSource.getMessage("message.atleastone", null, locale);
					model.addAttribute("error", msg);
		    	}		
		}
		if(OP_RESET.equalsIgnoreCase(operation)||OP_BACK.equalsIgnoreCase(operation))
	    {
	    	return "redirect:/ctl/Student/StudentListCtl";
	    }
		if(OP_NEW.equalsIgnoreCase(operation))
	    {
	    	return "redirect:/ctl/Student/AddStudent";
	    }
		
		
		pageNo=(pageNo<1)?1:pageNo;
	    form.setPageNo(pageNo);
	    
	    StudentDTO dto=(StudentDTO) form.getDto();
	    
	    List list=service.search(dto, pageNo, form.getPageSize());
	 	model.addAttribute("list",list);
	 	
	 	if(list.size()==0 && !OP_DELETE.equalsIgnoreCase(operation)){
	 		model.addAttribute("error", messageSource.getMessage("message.norecord", null, locale));
	 	}
	 	
		List next = service.search(dto, pageNo+1, form.getPageSize());
		model.addAttribute("nextlistsize",next.size());
	 	
		return "StudentListView"; 
	}
	
}

