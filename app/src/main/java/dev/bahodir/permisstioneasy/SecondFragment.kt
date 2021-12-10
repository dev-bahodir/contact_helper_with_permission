package dev.bahodir.permisstioneasy

import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import dev.bahodir.permisstioneasy.databinding.FragmentSecondBinding
import dev.bahodir.permisstioneasy.user.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "user"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var user: User? = null
    //private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(ARG_PARAM1) as User?
            //param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.name.text = user?.name
        binding.number.text = user?.number

        binding.send.setOnClickListener {
            if (binding.message.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Message is empty", Toast.LENGTH_SHORT).show()
            }
            else {
                var smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(user?.number,
                    null,
                    binding.message.text.toString(),
                    null,
                    null)
                Toast.makeText(requireContext(), "Sms was sent successfully", Toast.LENGTH_SHORT)
                    .show()
                findNavController().popBackStack()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param user Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SecondFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(user: User) =
            SecondFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, user)
                    //putString(ARG_PARAM2, param2)
                }
            }
    }
}