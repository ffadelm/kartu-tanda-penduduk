/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ti.umy.kartutandapenduduk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author MSI
 */
@Controller
public class WargaController {

    WargaJpaController wargacontroller = new WargaJpaController();
    List<Warga> warga = new ArrayList<>();

    @RequestMapping("/")
    public String getAll(Model model) {
        try {
            warga = wargacontroller.findWargaEntities();
        } catch (Exception ex) {

        }
        model.addAttribute("warga", warga);
        return "index";
    }

    @RequestMapping("/tambah")
    public String tambah() {
        return "tambah";
    }

    @PostMapping(value = "/simpan", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String simpan(@RequestParam("foto") MultipartFile file, HttpServletRequest req) throws Exception {
        Warga warga = new Warga();

        String nama = req.getParameter("nama");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("ttl"));
        byte[] img = file.getBytes();

        warga.setNama(nama);
        warga.setTtl(date);
        warga.setFoto(img);

        wargacontroller.create(warga);
        return "redirect:/";
    }

    //get foto by id
    @RequestMapping(value = "/img", method = RequestMethod.GET, produces = {
        MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE
    })
    public ResponseEntity<byte[]> getImg(@RequestParam("id") long id) throws Exception {
        Warga warga = wargacontroller.findWarga(id);
        byte[] img = warga.getFoto();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img);
    }

    @RequestMapping("/ubah/{id}")
    public String ubah(@PathVariable("id") long id, Model model) throws Exception {
        Warga warga = wargacontroller.findWarga(id);
        model.addAttribute("warga", warga);
        return "ubah";
    }

    @PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String ubahData(@RequestParam("foto") MultipartFile file, HttpServletRequest req) throws ParseException, Exception {
        Warga warga = new Warga();

        long id = Long.parseLong(req.getParameter("id"));
        String nama = req.getParameter("nama");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("ttl"));
        byte[] img = file.getBytes();
        warga.setId(id);
        warga.setNama(nama);
        warga.setTtl(date);
        warga.setFoto(img);

        wargacontroller.edit(warga);
        return "redirect:/";
    }

    @GetMapping("/hapus/{id}")
    public String hapus(@PathVariable("id") long id, Model model) throws Exception {
        wargacontroller.destroy(id);
        return "redirect:/";
    }
}
