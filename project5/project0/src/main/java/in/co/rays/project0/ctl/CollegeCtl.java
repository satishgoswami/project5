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

import in.co.rays.project0.dto.CollegeDTO;
import in.co.rays.project0.exception.DuplicateRecordException;
import in.co.rays.project0.form.CollegeForm;
import in.co.rays.project0.service.CollegeServiceInt;

@Controller
@RequestMapping(value="/ctl/College")
public class CollegeCtl extends BaseCtl
{
  
	@Autowired
	private CollegeServiceInt service;
	
	@Autowired
	private MessageSource messageSource;
	  @InitBinder
	    public void initBinder ( WebDataBinder binder )
	    {
	        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
	        binder.registerCustomEditor(String.class, stringtrimmer);
	    }

	  @Override
		public void preload(Model model) 
		{
		 List list1 = service.search(null);
		 model.addAttribute("collegeList", list1);
		 
		 }
	  
	  
	@RequestMapping(value="/AddCollege" , method=RequestMethod.GET)
	public String display(@RequestParam(required=false) Long id , @ModelAttribute("form") CollegeForm form , Model model,Locale locale)
	{
		String entername=messageSource.getMessage("label.entercname",null,locale);
		model.addAttribute("enterName",entername);
		
		String enteraddress=messageSource.getMessage("label.enteraddress",null,locale);
		model.addAttribute("enterAddress",enteraddress);
		
		String enterstate=messageSource.getMessage("label.enterstate",null,locale);
		model.addAttribute("enterState",enterstate);
		
		String entercity=messageSource.getMessage("label.entercity",null,locale);
		model.addAttribute("enterCity",entercity);
		
		String enterphone =messageSource.getMessage("label.enterphone",null,locale);
		model.addAttribute("enterPhoneNo",enterphone);
		
		System.out.println("Inside college ctl");
		if(id!=null && id>0)
		{
		 CollegeDTO dto = (CollegeDTO) service.findById(id);
		 form.populate(dto);
		}
		
		return "CollegeView";	
	}
	
	@RequestMapping(value="/AddCollege" , method=RequestMethod.POST)
	public String submit(@RequestParam(required=false) Long id,@ModelAttribute("form") @Valid CollegeForm form , BindingResult result , Model model , Locale locale) throws DuplicateRecordException
	{
		String entername=messageSource.getMessage("label.entercname",null,locale);
		model.addAttribute("enterName",entername);
		
		String enteraddress=messageSource.getMessage("label.enteraddress",null,locale);
		model.addAttribute("enterAddress",enteraddress);
		
		String enterstate=messageSource.getMessage("label.enterstate",null,locale);
		model.addAttribute("enterState",enterstate);
		
		String entercity=messageSource.getMessage("label.entercity",null,locale);
		model.addAttribute("enterCity",entercity);
		
		String enterphone =messageSource.getMessage("label.enterphone",null,locale);
		model.addAttribute("enterPhoneNo",enterphone);
		
		if(OP_SAVE.equalsIgnoreCase(form.getOperation()))
		{
			if(result.hasErrors())
			{
				return "CollegeView";
			}
			
			CollegeDTO dto = (CollegeDTO) form.getDto();
			dto.setCreatedBy("root");
			dto.setModifiedBy("root");
			
			
				try {
					
					if(id>0)
					{
						service.update(dto);
						String msg = messageSource.getMessage("message.updatesuccess", null, locale);
						model.addAttribute("success", msg);
					}else {
					
					service.add(dto);
					String msg = messageSource.getMessage("message.success", null, locale);
					model.addAttribute("success", msg);
					}
				} catch (DuplicateRecordException e) {
					String msg = messageSource.getMessage("error.collegename", null, locale);
					model.addAttribute("error", msg);
				}	
			}
		
		if(OP_RESET.equalsIgnoreCase(form.getOperation()))
		{
			return "redirect:/ctl/College/AddCollege";
		}
		if(OP_CANCEL.equalsIgnoreCase(form.getOperation()))
		{
			return "redirect:/ctl/College/CollegeListCtl";
		}
		
		return "CollegeView";
	}
	
	@RequestMapping(value="/CollegeListCtl" , method=RequestMethod.GET)
	public String display(@ModelAttribute("form") CollegeForm form,Model model,Locale locale)
	{
		String enterName = messageSource.getMessage("label.entercname", null, locale);
		model.addAttribute("enterName", enterName);
		
		int pageNo = 1;
		int pageSize = form.getPageSize();
		
		List l = service.search(new CollegeDTO(), pageNo, pageSize);
		model.addAttribute("list", l);
		
		List next = service.search(new CollegeDTO(), pageNo+1, pageSize);
		model.addAttribute("nextlistsize", next.size());
		
		return "CollegeListView";
	}
	
	@RequestMapping(value="/CollegeListCtl" , method=RequestMethod.POST)
	public String submit(@RequestParam(required=false) String operation,@ModelAttribute("form") CollegeForm form , BindingResult result , Model model , Locale locale)
	{
		String enterName = messageSource.getMessage("label.entercname", null, locale);
		model.addAttribute("enterName", enterName);
		
		int pageNo = (form.getPageNo()<1)?1:form.getPageNo();
		int pageSize = form.getPageSize();
		
		System.out.println("page No POST--->"+pageNo);
		
		List list = null;
		List next = null;
		
		CollegeDTO dto = (CollegeDTO) form.getDto();
		
		if(OP_SEARCH.equalsIgnoreCase(operation))
		{
			pageNo=1;	
		}
		if(OP_PREVIOUS.equalsIgnoreCase(operation))
		{
			pageNo--;
			System.out.println("page No POST--->"+pageNo);
		}
		if(OP_NEXT.equalsIgnoreCase(operation))
		{
			pageNo++;
			System.out.println("page No POST--->"+pageNo);
		}
		if(OP_DELETE.equalsIgnoreCase(operation))
		{
			if(form.getChk_1()!=null)
			{
				for(Long id : form.getChk_1())
				{
				  service.delete(id);
				}
				 String msg = messageSource.getMessage("message.deleterecord", null, locale);
					model.addAttribute("success", msg);
			}else
			{
				String msg = messageSource.getMessage("message.atleastone", null, locale);
				model.addAttribute("error", msg);
			}
		}
		if(OP_NEW.equalsIgnoreCase(operation))
		{
			return "redirect:/ctl/College/AddCollege";
		}
		if(OP_RESET.equalsIgnoreCase(operation))
		{
			return "redirect:/ctl/College/CollegeListCtl";
		}
		
		form.setPageNo(pageNo);
		
		list = service.search(dto, pageNo, pageSize);
		model.addAttribute("list", list);
		
		if(list.size()==0  &&  !OP_DELETE.equalsIgnoreCase(operation))
		{
			model.addAttribute("error", messageSource.getMessage("message.norecord", null, locale));
		}
		
		next = service.search(dto, pageNo+1, pageSize);
		model.addAttribute("nextlistsize", next.size());
		
		return "CollegeListView";
	}
	
}

