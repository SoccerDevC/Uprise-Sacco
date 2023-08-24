<?php
namespace App\Mail;

use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Mail\Mailable;
use Illuminate\Queue\SerializesModels;
use Illuminate\Support\Collection; // Import Collection class

class PerformanceReport extends Mailable
{
    use Queueable, SerializesModels;

    protected $members;
    protected $deposits;
    protected $registeredLoans;

    public function __construct(Collection $members, Collection $deposits, Collection $registeredLoans)
    {
        $this->members = $members;
        $this->deposits = $deposits;
        $this->registeredLoans = $registeredLoans;
    }

    public function build()
    {
        return $this->subject('Uprise Sacco Performance Report')
            ->view('pages.report')
            ->with([
                'members' => $this->members,
                'deposits' => $this->deposits,
                'registeredLoans' => $this->registeredLoans,
            ]);
    }
}
