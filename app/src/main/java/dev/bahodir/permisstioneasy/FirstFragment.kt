package dev.bahodir.permisstioneasy

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import dev.bahodir.permisstioneasy.adapter.RVAdapter
import dev.bahodir.permisstioneasy.databinding.FragmentFirstBinding
import dev.bahodir.permisstioneasy.user.User
import com.karumi.dexter.PermissionToken

import com.karumi.dexter.listener.PermissionDeniedResponse

import com.karumi.dexter.listener.PermissionGrantedResponse

import com.karumi.dexter.listener.single.PermissionListener

import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FirstFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var rvAdapter: RVAdapter
    private lateinit var list: MutableList<User>
    private lateinit var matchedPeople: MutableList<User>
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        //checkPermission()

        list = mutableListOf()
        matchedPeople = mutableListOf()
        rvAdapter = RVAdapter(list, object : RVAdapter.OnItemTouchClickListener {
            var count1 = 0
            override fun onCall(user: User, position: Int) {
                Toast.makeText(requireContext(), "Call clicked", Toast.LENGTH_SHORT).show()

                Dexter.withContext(requireContext())
                    .withPermission(Manifest.permission.CALL_PHONE)
                    .withListener(object : PermissionListener {

                        override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                            startCall(user = user)
                        }

                        override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                            if (count1 > 0) {
                                var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                var uri: Uri =
                                    Uri.fromParts("package", requireContext().packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            }
                            count1++
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?,
                            p1: PermissionToken?,
                        ) {
                            p1?.continuePermissionRequest()
                        }

                    }).check()
            }
            var count2 = 0
            override fun onSms(user: User, position: Int) {
                Toast.makeText(requireContext(), "Sms clicked", Toast.LENGTH_SHORT).show()
                Dexter.withContext(requireContext())
                    .withPermission(Manifest.permission.SEND_SMS)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                            var bundle = Bundle()
                            bundle.putSerializable("user", user)
                            findNavController().navigate(R.id.action_firstFragment_to_secondFragment,
                                bundle)

                        }

                        override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                            if (count2 > 0) {
                                var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                var uri: Uri =
                                    Uri.fromParts("package", requireContext().packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            }

                            count2++
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?,
                            p1: PermissionToken?,
                        ) {
                            p1?.continuePermissionRequest()
                        }

                    }).check()
            }

        })

        binding.recycler.adapter = rvAdapter

        binding.search.isSubmitButtonEnabled = true
        performSearch()

        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    getContacts()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(requireContext(),
                        "Permission should be shown",
                        Toast.LENGTH_SHORT).show()
                    var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    var uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
                    intent.data = uri
                    startActivity(intent)

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?,
                ) {
                    p1?.continuePermissionRequest()
                }

            }).check()
        return binding.root
    }

    /*override fun onStart() {
        super.onStart()
        rvAdapter.notifyDataSetChanged()
    }*/

    /*override fun onResume() {
        super.onResume()
        rvAdapter.notifyDataSetChanged()
    }*/
        /*rvAdapter = RVAdapter(list, object : RVAdapter.OnItemTouchClickListener {
            var count1 = 0
            override fun onCall(user: User, position: Int) {
                Toast.makeText(requireContext(), "Call clicked", Toast.LENGTH_SHORT).show()

                Dexter.withContext(requireContext())
                    .withPermission(Manifest.permission.CALL_PHONE)
                    .withListener(object : PermissionListener {

                        override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                            startCall(user = user)
                        }

                        override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                            if (count1 > 0) {
                                var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                var uri: Uri =
                                    Uri.fromParts("package", requireContext().packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            }
                            count1++
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?,
                            p1: PermissionToken?,
                        ) {
                            p1?.continuePermissionRequest()
                        }

                    }).check()
            }
            var count2 = 0
            override fun onSms(user: User, position: Int) {
                Toast.makeText(requireContext(), "Sms clicked", Toast.LENGTH_SHORT).show()
                Dexter.withContext(requireContext())
                    .withPermission(Manifest.permission.SEND_SMS)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                            var bundle = Bundle()
                            bundle.putSerializable("user", user)
                            findNavController()
                            findNavController().navigate(R.id.action_firstFragment_to_secondFragment,
                                bundle)

                        }

                        override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                            if (count2 > 0) {
                                var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                var uri: Uri =
                                    Uri.fromParts("package", requireContext().packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            }

                            count2++
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?,
                            p1: PermissionToken?,
                        ) {
                            p1?.continuePermissionRequest()
                        }

                    }).check()
            }

        })

        binding.recycler.adapter = rvAdapter
        rvAdapter.notifyDataSetChanged()*//*
        *//*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) ==
                    PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_CONTACTS)) {

                }
                else {
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf({Manifest.permission.READ_CONTACTS}.toString()), 1)
                }
            }
            else if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED){
                getContacts()
            }
        }
        else {
            getContacts()
        }*//*
    }*/

    /*override fun onStart() {
        super.onStart()
        list = mutableListOf()
        matchedPeople = mutableListOf()
        rvAdapter = RVAdapter(list, object : RVAdapter.OnItemTouchClickListener {
            var count1 = 0
            override fun onCall(user: User, position: Int) {
                Toast.makeText(requireContext(), "Call clicked", Toast.LENGTH_SHORT).show()

                Dexter.withContext(requireContext())
                    .withPermission(Manifest.permission.CALL_PHONE)
                    .withListener(object : PermissionListener {

                        override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                            startCall(user = user)
                        }

                        override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                            if (count1 > 0) {
                                var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                var uri: Uri =
                                    Uri.fromParts("package", requireContext().packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            }
                            count1++
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?,
                            p1: PermissionToken?,
                        ) {
                            p1?.continuePermissionRequest()
                        }

                    }).check()
            }
            var count2 = 0
            override fun onSms(user: User, position: Int) {
                Toast.makeText(requireContext(), "Sms clicked", Toast.LENGTH_SHORT).show()
                Dexter.withContext(requireContext())
                    .withPermission(Manifest.permission.SEND_SMS)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                            var bundle = Bundle()
                            bundle.putSerializable("user", user)
                            findNavController().navigate(R.id.action_firstFragment_to_secondFragment,
                                bundle)

                        }

                        override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                            if (count2 > 0) {
                                var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                var uri: Uri =
                                    Uri.fromParts("package", requireContext().packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            }

                            count2++
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?,
                            p1: PermissionToken?,
                        ) {
                            p1?.continuePermissionRequest()
                        }

                    }).check()
            }

        })

        binding.recycler.adapter = rvAdapter

        binding.search.isSubmitButtonEnabled = true
        performSearch()

        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    getContacts()

                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(requireContext(),
                        "Permission should be shown",
                        Toast.LENGTH_SHORT).show()
                    var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    var uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
                    intent.data = uri
                    startActivity(intent)

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?,
                ) {
                    p1?.continuePermissionRequest()
                }

            }).check()

    }*/

    private fun performSearch() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return true
            }
        })
    }

    private fun search(text: String?) {
        /*var matchedPeople = mutableListOf<User>()*/

        text?.let {
            list.forEach { user ->
                if (user.name?.contains(text, true)!! || user.number?.contains(text, true)!!
                ) {
                    matchedPeople.add(user)
                }
            }
            updateRecyclerView()
            if (matchedPeople.isEmpty()) {
                Toast.makeText(requireContext(), "No match found!", Toast.LENGTH_SHORT).show()
            }
            updateRecyclerView()
        }
    }
    private fun updateRecyclerView() {
        binding.recycler.apply {
            rvAdapter.list = matchedPeople
            rvAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startCall(user: User? = null) {
        Toast.makeText(requireContext(), "start call", Toast.LENGTH_SHORT).show()
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel: ${user?.number}")
        startActivity(callIntent)
    }

    @SuppressLint("Range")
    private fun getContacts() {
        /*list = mutableListOf()*/
        var phones = activity?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        while (phones?.moveToNext()!!) {
            var name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            var number =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            list.add(User(name, number))
            rvAdapter.notifyDataSetChanged()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1)
            startCall()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FirstFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}